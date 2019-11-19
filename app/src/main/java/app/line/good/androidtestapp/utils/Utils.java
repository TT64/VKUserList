package app.line.good.androidtestapp.utils;

import app.line.good.androidtestapp.R;

public class Utils {
    private Utils() {
    }

    public static int getUserSex(int sex) {
        if (sex == 1) {
            return R.string.user_female;
        } else if (sex == 0) {
            return R.string.user_anymale;
        } else
            return R.string.user_male;
    }

    public static int getUserRelation(int relation, int userSex) {
        if (relation == 1) {
            if (userSex == 0)
                return R.string.not_married_anymale;
            if (userSex == 1)
                return R.string.not_married_female;
            if (userSex == 2)
                return R.string.not_married_male;
        }
        if (relation == 2) {
            if (userSex == 0)
                return R.string.have_friend_anymale;
            if (userSex == 1)
                return R.string.have_friend_female;
            if (userSex == 2)
                return R.string.have_friend_male;
        }
        if (relation == 3) {
            if (userSex == 0)
                return R.string.engaged_anymale;
            if (userSex == 1)
                return R.string.engaged_female;
            if (userSex == 2)
                return R.string.engaged_male;
        }
        if (relation == 4) {
            if (userSex == 0)
                return R.string.married_anymale;
            if (userSex == 1)
                return R.string.married_female;
            if (userSex == 2)
                return R.string.married_male;
        }
        if (relation == 5)
            return R.string.all_difficult;
        if (relation == 6)
            return R.string.active_searching;
        if (relation == 7) {
            if (userSex == 0)
                return R.string.inlove_anymale;
            if (userSex == 1)
                return R.string.inlove_female;
            if (userSex == 2)
                return R.string.inlove_male;
        }
        if (relation == 8)
            return R.string.civil_marriage;
        else
            return R.string.relation_not_specified;
    }
}
