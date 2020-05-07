package net.zdsoft.gkelective.data.action.optaplanner.func;

import org.drools.core.spi.KnowledgeHelper;
/**
 * drools 调试用
 * @author shensiping
 *
 */
public class DroolsDebugUtility {
	
	public static void print(final KnowledgeHelper drools, final String message) {
		System.out.println(message);
		System.out.println("\nrule triggered: " + drools.getRule().getName());
	}

	public static void print(final KnowledgeHelper drools) {
		System.out.println("\nrule triggered: " + drools.getRule().getName());
	}
}
