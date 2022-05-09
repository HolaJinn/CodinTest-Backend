package com.proxym.yacine.codintest.util;

public interface Routes {

    public static String mainRoute = "/api/v1";
    public static String authRoute = mainRoute + "/auth";
    public static String registerRoute = "/register";
    public static String registerOwnerRoute = "/owner/register";
    public static String loginRoute = "/login";
    public static String verifyAccountRoute = "/verify";
    public static String forgotPasswordRoute = "/forgot-password";
    public static String verifyResetPasswordRoute = "/verify-reset-password";
    public static String resetPasswordRoute = "/reset-password";
    public static String currentUserRoute = "/current";
    public static String exercisesRoute = mainRoute + "/exercises";
    public static String programmingLanguagesRoute = mainRoute + "/programming-languages";
    public static String programmingLanguagesByNameRoute = "/by-name";
    public static String tagsRoute = mainRoute + "/tags";
    public static String tagsByNameRoute = "/by-name";
    public static String ownerRoute = mainRoute + "/owner";
    public static String companyRoute = mainRoute + "/company";
    public static String technicalTestRoute = mainRoute + "/technical-tests";
    public static String invitationsRoute = mainRoute + "/invitations";
    public static String judgeRoute = mainRoute + "/judge";
}
