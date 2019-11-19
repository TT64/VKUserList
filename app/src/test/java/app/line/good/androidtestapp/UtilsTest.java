package app.line.good.androidtestapp;

import org.junit.Test;

import app.line.good.androidtestapp.utils.Utils;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void utilsGetUserSexTest(){
        assertEquals("user sex must be not specified", R.string.user_anymale, Utils.getUserSex(0));
        assertEquals("user sex must be female", R.string.user_female, Utils.getUserSex(1));
        assertEquals("user sex must be male", R.string.user_male, Utils.getUserSex(2));
    }

    @Test
    public void utilsGetUserRelationTest(){
        assertEquals("user relation must be not married anymale", R.string.not_married_anymale, Utils.getUserRelation(1, 0));
        assertEquals("user relation must be not married female", R.string.not_married_female, Utils.getUserRelation(1, 1));
        assertEquals("user relation must be not married male", R.string.not_married_male, Utils.getUserRelation(1, 2));
        assertEquals("user relation must be have friend anymale", R.string.have_friend_anymale, Utils.getUserRelation(2, 0));
        assertEquals("user relation must be have friend female", R.string.have_friend_female, Utils.getUserRelation(2, 1));
        assertEquals("user relation must be have friend male", R.string.have_friend_male, Utils.getUserRelation(2, 2));
        assertEquals("user relation must be engaged anymale", R.string.engaged_anymale, Utils.getUserRelation(3, 0));
        assertEquals("user relation must be engaged female", R.string.engaged_female, Utils.getUserRelation(3, 1));
        assertEquals("user relation must be engaged male", R.string.engaged_male, Utils.getUserRelation(3, 2));
        assertEquals("user relation must be married anymale", R.string.married_anymale, Utils.getUserRelation(4, 0));
        assertEquals("user relation must be married female", R.string.married_female, Utils.getUserRelation(4, 1));
        assertEquals("user relation must be married male", R.string.married_male, Utils.getUserRelation(4, 2));
        assertEquals("user relation must be all difficult anymale", R.string.all_difficult, Utils.getUserRelation(5, 0));
        assertEquals("user relation must be all difficult anymale", R.string.all_difficult, Utils.getUserRelation(5, 1));
        assertEquals("user relation must be all difficult anymale", R.string.all_difficult, Utils.getUserRelation(5, 2));
        assertEquals("user relation must be active searching anymale", R.string.active_searching, Utils.getUserRelation(6, 0));
        assertEquals("user relation must be active searching anymale", R.string.active_searching, Utils.getUserRelation(6, 1));
        assertEquals("user relation must be active searching anymale", R.string.active_searching, Utils.getUserRelation(6, 2));
        assertEquals("user relation must be inlove anymale", R.string.inlove_anymale, Utils.getUserRelation(7, 0));
        assertEquals("user relation must be inlove female", R.string.inlove_female, Utils.getUserRelation(7, 1));
        assertEquals("user relation must be inlove male", R.string.inlove_male, Utils.getUserRelation(7, 2));
        assertEquals("user relation must be civil marriage anymale", R.string.civil_marriage, Utils.getUserRelation(8, 0));
        assertEquals("user relation must be civil marriage anymale", R.string.civil_marriage, Utils.getUserRelation(8, 1));
        assertEquals("user relation must be civil marriage anymale", R.string.civil_marriage, Utils.getUserRelation(8, 2));
        assertEquals("user relation must be not specified", R.string.relation_not_specified, Utils.getUserRelation(9, 0));
        assertEquals("user relation must be not specified", R.string.relation_not_specified, Utils.getUserRelation(9, 1));
        assertEquals("user relation must be not specified", R.string.relation_not_specified, Utils.getUserRelation(9, 2));
    }

}