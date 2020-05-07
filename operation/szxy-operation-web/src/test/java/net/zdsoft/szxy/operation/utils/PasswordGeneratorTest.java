package net.zdsoft.szxy.operation.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author shenke
 * @since 2019/2/15 下午4:27
 */
public class PasswordGeneratorTest {

    @Test
    public void rule_2() {
        String pwd = PasswordGenerator.generateStudentAndFamilyPassword(PasswordGenerator.PasswordRule.S_RULE_2,
                PasswordGenerator.KeyInfo.identityCard("341124199501181034"));
        Assert.assertEquals("181034", pwd);
    }

    @Test
    public void rule_3() {
        String pwd = PasswordGenerator.generateStudentAndFamilyPassword(PasswordGenerator.PasswordRule.S_RULE_3,
                PasswordGenerator.KeyInfo.username("shengld"));
        Assert.assertEquals("hengld", pwd);

        pwd = PasswordGenerator.generateStudentAndFamilyPassword(PasswordGenerator.PasswordRule.S_RULE_3,
                PasswordGenerator.KeyInfo.username("ngld"));
        Assert.assertEquals("00ngld", pwd);
    }

    @Test
    public void rule_4() {
        String pwd = PasswordGenerator.generateStudentAndFamilyPassword(PasswordGenerator.PasswordRule.S_RULE_4,
                null);
        Assert.assertEquals(6, pwd.length());
    }
}
