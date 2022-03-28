package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.config.security.TokenProvider;
import com.proxym.yacine.codintest.dto.request.*;
import com.proxym.yacine.codintest.dto.response.AuthenticationResponse;
import com.proxym.yacine.codintest.dto.response.CurrentUserDto;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.*;
import com.proxym.yacine.codintest.repository.*;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.RoleService;
import com.proxym.yacine.codintest.util.Constants;
import com.proxym.yacine.codintest.util.UserState;
import com.proxym.yacine.codintest.validator.AppUserValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    @Autowired @Lazy
    private AppUserRepository appUserRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ModelMapper modelMapper;


    /*
    * UserDetailsService implementation
    * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = findByEmail(username);
        if ( user.getState().equals(UserState.INACTIVE)) {
            throw new CustomException("Failed to load user","USER IS DEACTIVATED",401);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), getAuthority(user)
        );
    }

    @Override
    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new CustomException("No user found with such email", "USER NOT FOUND", 404));
    }

    @Override
    public AppUser findById(Long id) {
        return appUserRepository.findById(id).orElseThrow(
                () -> new CustomException("No user found with such ID", "USER NOT FOUND", 404)
        );
    }

    private Set<SimpleGrantedAuthority> getAuthority(AppUser user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorities;
    }

    @Override
    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    /*
     * This method is used to save a new user after registration
     * First check if the user registration info are valid if not an exception will be thrown
     * Second it creates a random code to be added to the verification code for the new user
     * The verificationCode is a field in the AppUser entity
     * This field is a string that contains 64 random characters
     * This is made to guarantee that two user can not have the same verification code in the same time
     * It then check the role to know in which table the new user will be created
     * By default the user state will be active, and he will verify his email instantly
     * A verification link will be sent to his email
     * */
    @Override
    public void saveAppUser(NewAppUser newAppUser, String siteURL) throws MessagingException, UnsupportedEncodingException {
        checker(newAppUser);
        String randomCode = RandomString.make(64);
        Role role = roleService.findByName(newAppUser.getRole());
        if (role.getId() == 1) {
            AppUser superAdmin = AppUser.builder()
                    .email(newAppUser.getEmail())
                    .firstName(newAppUser.getFirstName())
                    .lastName(newAppUser.getLastName())
                    .password(bCryptPasswordEncoder.encode(newAppUser.getPassword()))
                    .photoURL("Default photo")
                    .state(UserState.ACTIVE)
                    .verified(true)
                    .company(null)
                    .roleInCompany(null)
                    .role(role)
                    .build();
            appUserRepository.save(superAdmin);
        }

        if (role.getId() == 4) {
            AppUser candidate = AppUser.builder()
                    .email(newAppUser.getEmail())
                    .firstName(newAppUser.getFirstName())
                    .lastName(newAppUser.getLastName())
                    .password(bCryptPasswordEncoder.encode(newAppUser.getPassword()))
                    .photoURL("Default photo")
                    .state(UserState.ACTIVE)
                    .verified(false)
                    .company(null)
                    .roleInCompany(null)
                    .role(role)
                    .verificationCode(randomCode)
                    .build();
            appUserRepository.save(candidate);
            sendVerificationCode(newAppUser, "candidate", randomCode);
        }
        log.info(String.format("Registration success for user { email: %s } at %s",newAppUser.getEmail(), LocalDateTime.now().toString()));
    }

    /*
    * This method unlike the saveAppUser method will only save company owners.
    * Because it is provided with information about the company.
    * */
    @Override
    public void saveCompanyOwner(NewCompanyOwner newCompanyOwner, String siteURL) throws MessagingException, UnsupportedEncodingException {
        checker(newCompanyOwner);
        String randomCode = RandomString.make(64);
        Role role = roleService.findByName(newCompanyOwner.getRole());
        AppUser owner = AppUser.builder()
                .email(newCompanyOwner.getEmail())
                .firstName(newCompanyOwner.getFirstName())
                .lastName(newCompanyOwner.getLastName())
                .password(bCryptPasswordEncoder.encode(newCompanyOwner.getPassword()))
                .photoURL("Default photo")
                .state(UserState.ACTIVE)
                .verified(false)
                .roleInCompany(newCompanyOwner.getRoleInCompany())
                .role(role)
                .verificationCode(randomCode)
                .build();
        appUserRepository.save(owner);
        sendVerificationCode(newCompanyOwner, "owner", randomCode);

        Company company = Company.builder()
                .owner(owner)
                .name(newCompanyOwner.getCompanyName())
                .country(newCompanyOwner.getCountry())
                .isActive(true)
                .subscriptionDurationInDays(14)
                .build();

        companyRepository.save(company);
        AppUser newOwner = appUserRepository.findById(owner.getId()).orElseThrow(() -> new CustomException("No owner found with such ID", "USER NOT FOUND", 404));
        newOwner.setCompany(company);
        appUserRepository.save(newOwner);

        log.info(String.format("Registration success for user { email: %s } at %s",newCompanyOwner.getEmail(), LocalDateTime.now().toString()));
    }


    /*
    * This method allow the company owner to create a new recruiter and save it to it's related company
    * */
    @Override
    public void saveRecruiter(NewRecruiter newRecruiter,String siteURL) throws MessagingException, UnsupportedEncodingException {
        AppUser owner = getCurrentAuthenticatedUser();
        checker(newRecruiter);
        String randomCode = RandomString.make(64);
        Role role = roleService.findByName(newRecruiter.getRole());

        if (owner.getRole().getId().intValue() != 2) {
            throw new CustomException("You are unauthorized to add new recruiter", "UNAUTHORIZED", 403);
        }

        AppUser recruiter = AppUser.builder()
                .email(newRecruiter.getEmail())
                .firstName(newRecruiter.getFirstName())
                .lastName(newRecruiter.getLastName())
                .password(bCryptPasswordEncoder.encode(newRecruiter.getPassword()))
                .photoURL("Default photo")
                .state(UserState.ACTIVE)
                .verified(false)
                .company(owner.getCompany())
                .roleInCompany(newRecruiter.getRoleInCompany())
                .role(role)
                .verificationCode(randomCode)
                .build();
        appUserRepository.save(recruiter);
        sendVerificationCode(newRecruiter,siteURL,randomCode);

    }

    /*
     * This method is used to send a verification code by email
     * This email will contain a redirection link for a new URL path that will contain the code as a parameter
     * */
    @Override
    public void sendVerificationCode(NewAppUser newAppUser, String siteURL, String code) throws MessagingException, UnsupportedEncodingException  {
        String toAddress = newAppUser.getEmail();
        String fromAddress = "benamoryacine98@gmail.com";
        String senderName = "CodinTest";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "CodinTest.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", newAppUser.getFirstName());
//        String verifyURL = siteURL + "/auth/verify?code=" + code;
        String verifyURL = Constants.CLIENT_URL + "auth/verify?code=" + code;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }

    /*
     * This method is used to verify the verification code provided by the user
     * The method will use the repository to find the user using that verification code
     * If it exists it means that the code is correct
     * In that case the code will be reset to null and the user will be verified
     * If not it will return false
     * */
    @Override
    public boolean verify(String verificationCode) {
        AppUser appUser = appUserRepository.findByVerificationCode(verificationCode);
        if (appUser == null || appUser.isVerified()) {
            log.error("Could not verify");
            return false;
        } else {
            appUser.setVerificationCode(null);
            appUser.setVerified(true);
            appUserRepository.save(appUser);
            log.info("Email verified successfully");
            return true;
        }
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            );
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(String.format("Authentication success for user { login: %s } at %s",request.getEmail(), LocalDateTime.now().toString()));
            AuthenticationResponse response = new AuthenticationResponse();
            response.setJwtToken(tokenProvider.generateToken(authentication));
            return response;
        }
        catch (Exception e ){
            throw new CustomException(e.getMessage(),"AUTHENTICATION FAILED", 400);
        }
    }

    @Override
    public AppUser getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(username);
    }

    @Override
    public CurrentUserDto getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = findByEmail(username);
        return modelMapper.map(user, CurrentUserDto.class);
    }

    @Override
    public void updateResetPasswordToken(ForgotPasswordRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException {
        AppUser appUser = findByEmail(request.getEmail());
        if(appUser != null) {
            String token = RandomString.make(64);
            appUser.setResetPasswordToken(token);
            appUserRepository.save(appUser);
            sendResetPasswordToken(request.getEmail(), token, siteURL);
        }
    }

    /*
     * This method is used to send a reset password token by email to the user who wanted to reset his password
     * This email will contain a redirection link for a new URL path that will contain the token as a parameter
     * */
    @Override
    public void sendResetPasswordToken(String email, String token, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String fromAddress = "benamoryacine98@gmail.com";
        String senderName = "CodinTest";
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                +"<h3><a href=\"[[URL]]\" target=\"_self\">Change my password</a></h3>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        //String verifyURL = siteURL + "/auth/verify-reset-password?token=" + token;

        String verifyURL = "http://localhost:3000/reset-password?token=" + token;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public boolean verifyResetPasswordToken(String token) {
        AppUser appUser = appUserRepository.findByResetPasswordToken(token);
        if (appUser == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void updatePassword(String token, NewPasswordRequest request) {
        AppUser appUser = appUserRepository.findByResetPasswordToken(token);
        if (appUser != null) {
            if (request.getNewPassword().length() > 5) {
                appUser.setPassword("");
                appUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
                appUser.setResetPasswordToken(null);
                appUserRepository.save(appUser);
            } else {
                throw new CustomException("Your password should contain at least 6 characters", "INVALID INFO", 400);
            }

        } else {
            throw new CustomException("No user found");
        }

    }


    /*
    * This checker method is used to validate user's input for registration
    * It uses the AppUserValidator class in the validator package
    * */
    private void checker (NewAppUser newAppUser) {
        List<String> errors = AppUserValidator.validate(newAppUser);
        if (!errors.isEmpty()) {
            throw new CustomException("Invalid info provided","INVALID_INFO",errors, 400);
        }

        if (appUserRepository.existsByEmail(newAppUser.getEmail())) {
            throw new CustomException("This email already exists", "EMAIL_EXISTS", 400);
        }
    }
}
