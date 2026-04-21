package com.alessiodp.parties.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TagUtilsTest {
	@Test
	public void testStripLegacyFormatting() {
		Assertions.assertEquals("ABC", TagUtils.stripFormatting("&aAB&lC"));
	}

	@Test
	public void testStripMiniMessageFormatting() {
		Assertions.assertEquals("ABC", TagUtils.stripFormatting("<red>AB</red><bold>C</bold>"));
	}

	@Test
	public void testNormalizeMiniMessageIntoLegacy() {
		Assertions.assertEquals("&aABC", TagUtils.normalizeColorFormatting("<green>ABC"));
		Assertions.assertEquals("&#aabbccABC", TagUtils.normalizeColorFormatting("<#AABBCC>ABC"));
	}

	@Test
	public void testVisibleLength() {
		Assertions.assertEquals(3, TagUtils.visibleLength("<green>&lABC"));
	}

	@Test
	public void testFirstLetters() {
		Assertions.assertEquals("ATO", TagUtils.firstLetters("ATOMIC", 3));
		Assertions.assertEquals("GUI", TagUtils.firstLetters("Guild_123", 3));
	}
}

