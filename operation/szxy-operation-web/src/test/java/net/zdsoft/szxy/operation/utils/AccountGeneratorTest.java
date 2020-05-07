package net.zdsoft.szxy.operation.utils;

import net.zdsoft.szxy.base.enu.FamilyRelationCode;
import net.zdsoft.szxy.base.enu.UserOwnerTypeCode;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 学生、家长：
 *  * 1.(学生 s, 家长 p)+学校编号+6位随机数；{(s/p)_etohSchoolId_random(6)}
 *  * 2.(学生 s, 家长 p)+学校编号取前6位+姓名拼音；{(s/p)_etohSchoolId[0,6]_firstSpell}
 *  * 3.学生：用户名前缀+学号，家长：p+学校编号+6位随机数；{N_userNamePrefix_stuCode},{p_etohSchoolId_random(6)}
 *  * 4.学生：x+父亲手机号，父亲：f+自己手机号, 母亲 ：m+自己手机号；{x_fMobile},{(f/m)_mobile}
 *  * 5.学生：用户名前缀+学生所在班级编号+两位流水号，父亲：f+学生用户名，母亲：m+学生用户名; {N_userNamePrefix_clsCode_maxcode},{(f/m)_tempUserName}
 *  * 6.学生：G+身份证号，父亲：F+学生身份证号，母亲：M+学生身份证号，监护人：H+学生身份证号 ；{(G/F/M/H)_identitycard}
 *  * 7.学生：s+身份证号，父亲：f+学生身份证号，母亲：m+学生身份证号，监护人：g+学生身份证号；{(s/f/m/g)_identitycard}
 *  * 8.学生：s+学籍号，父亲：f+学生学籍号，母亲：m+学生学籍号，监护人：g+学生学籍号；{(s/f/m/g)_unitiveCode}
 *  * 9.学生：身份证号，父亲：f+学生身份证号，母亲：m+学生身份证号，监护人：g+学生身份证号 ；{N_identitycard},{(f/m/g)_identitycard}
 *  * 10-x：x为数字，表示学籍号后几位，学生：s+学籍号后x位，父亲：f+学生学籍号后x位，母亲：m+学生学籍号后x位，监护人：g+学生学籍号后x位；{(s/f/m/g)_unitiveCode[-1,x]}
 * @author shenke
 * @since 2019/2/15 下午2:02
 */

public class AccountGeneratorTest {

    private AccountGenerator.AccountInfo studentAccountInfo;
    private AccountGenerator.AccountInfo familyAccountInfo;

    @Before
    public void createAccountInfo() {
        studentAccountInfo = new AccountGenerator.AccountInfo();
        studentAccountInfo.setFirstSpell("student");
        studentAccountInfo.setUserNamePrefix("T");
        studentAccountInfo.setUnitiveCode("111111111111111");
        studentAccountInfo.setFMobile("15258828767");
        studentAccountInfo.setMaxcode("20");
        studentAccountInfo.setIdentitycard("342224198501181024");
        studentAccountInfo.setEtohSchoolId("000000000000000");
        studentAccountInfo.setOwnerType(UserOwnerTypeCode.STUDENT);
        studentAccountInfo.setClsCode("2019021509");
        studentAccountInfo.setStuCode("9999999999");

        familyAccountInfo = new AccountGenerator.AccountInfo();
        familyAccountInfo.setFirstSpell("family");
        familyAccountInfo.setUserNamePrefix("F");
        familyAccountInfo.setUnitiveCode("111111111111111");
        familyAccountInfo.setIdentitycard("342224198501181024");
        familyAccountInfo.setEtohSchoolId("000000000000000");
        familyAccountInfo.setOwnerType(UserOwnerTypeCode.FAMILY);
        familyAccountInfo.setClsCode("2019021509");
        familyAccountInfo.setStuCode("9999999999");
        familyAccountInfo.setMobile("15258828767");
        familyAccountInfo.setTempUserName("studentTemUserName");
        familyAccountInfo.setFamilyType(FamilyRelationCode.FATHER);
    }

    //
    @Test
    public void testRule_1() throws AccountGenerator.IllegalAccountRuleException {
        String rule = "{(s/p)_etohSchoolId_random(6)}";

        String username = AccountGenerator.generateAccount(rule, studentAccountInfo);
        Assert.assertEquals(username.length(), (1 + studentAccountInfo.getEtohSchoolId().length() + 6));
        Assert.assertTrue(StringUtils.startsWith(username, "s" + studentAccountInfo.getEtohSchoolId()));

        String familyUsername = AccountGenerator.generateAccount(rule, familyAccountInfo);
        Assert.assertEquals(familyUsername.length(), (1 + familyAccountInfo.getEtohSchoolId().length() + 6));
        Assert.assertTrue(StringUtils.startsWith(familyUsername, "p" + familyAccountInfo.getEtohSchoolId()));
    }

    @Test
    public void testRule_2() throws AccountGenerator.IllegalAccountRuleException {
        String rule = "{(s/p)_etohSchoolId[0,6]_firstSpell}";

        String username = AccountGenerator.generateAccount(rule, studentAccountInfo);
        String expected = "s" + studentAccountInfo.getEtohSchoolId().substring(0, 6) + studentAccountInfo.getFirstSpell();
        Assert.assertEquals(expected, username);

        String familyUsername = AccountGenerator.generateAccount(rule, familyAccountInfo);
        String familyExpected = "p" + familyAccountInfo.getEtohSchoolId().substring(0, 6) + familyAccountInfo.getFirstSpell();
        Assert.assertEquals(familyExpected, familyUsername);
    }

    @Test
    public void testRule_3() throws AccountGenerator.IllegalAccountRuleException {
        String rule = "{N_userNamePrefix_stuCode},{p_etohSchoolId_random(6)}";

        String username = AccountGenerator.generateAccount(rule, studentAccountInfo);
        String expected = studentAccountInfo.getUserNamePrefix() + studentAccountInfo.getStuCode();
        Assert.assertEquals(expected, username);

        String familyUsername = AccountGenerator.generateAccount(rule, familyAccountInfo);
        Assert.assertTrue(familyUsername.startsWith("p" + familyAccountInfo.getEtohSchoolId()));
    }

    @Test
    public void testRule_4() throws AccountGenerator.IllegalAccountRuleException {
        String rule = "{x_fMobile},{(f/m)_mobile}";
        //String username = AccountGenerator.generateAccount(rule, studentAccountInfo);
        //Assert.assertEquals("x" + studentAccountInfo.getFMobile(), username);
        //
        //familyAccountInfo.setFamilyType(UserRelationCode.FATHER);
        //String fatherUsername = AccountGenerator.generateAccount(rule, familyAccountInfo);
        //Assert.assertEquals("f" + familyAccountInfo.getMobile(), fatherUsername);

        familyAccountInfo.setFamilyType(FamilyRelationCode.MOTHER);
        String motherUsername = AccountGenerator.generateAccount(rule, familyAccountInfo);
        Assert.assertEquals("m" + familyAccountInfo.getMobile(), motherUsername);
    }
}
