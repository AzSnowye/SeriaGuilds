package com.alessiodp.parties.common.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public final class TagUtils {
	private static final int HEX_COLOR_LENGTH = 7;
	private static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("(?i)[&§][0-9A-FK-ORX]");
	private static final Pattern HEX_AMP_PATTERN = Pattern.compile("(?i)&#[0-9A-F]{6}");
	private static final Pattern HEX_SECTION_PATTERN = Pattern.compile("(?i)§#[0-9A-F]{6}");
	private static final Pattern LEGACY_HEX_PATTERN = Pattern.compile("(?i)[&§]x(?:[&§][0-9A-F]){6}");
	private static final Pattern MINIMESSAGE_PATTERN = Pattern.compile("(?i)</?[a-z0-9_!?#:-]+>");
	private static final Map<String, Character> MINIMESSAGE_TO_LEGACY = new HashMap<>();

	static {
		MINIMESSAGE_TO_LEGACY.put("black", '0');
		MINIMESSAGE_TO_LEGACY.put("dark_blue", '1');
		MINIMESSAGE_TO_LEGACY.put("dark_green", '2');
		MINIMESSAGE_TO_LEGACY.put("dark_aqua", '3');
		MINIMESSAGE_TO_LEGACY.put("dark_red", '4');
		MINIMESSAGE_TO_LEGACY.put("dark_purple", '5');
		MINIMESSAGE_TO_LEGACY.put("gold", '6');
		MINIMESSAGE_TO_LEGACY.put("gray", '7');
		MINIMESSAGE_TO_LEGACY.put("grey", '7');
		MINIMESSAGE_TO_LEGACY.put("dark_gray", '8');
		MINIMESSAGE_TO_LEGACY.put("dark_grey", '8');
		MINIMESSAGE_TO_LEGACY.put("blue", '9');
		MINIMESSAGE_TO_LEGACY.put("green", 'a');
		MINIMESSAGE_TO_LEGACY.put("aqua", 'b');
		MINIMESSAGE_TO_LEGACY.put("red", 'c');
		MINIMESSAGE_TO_LEGACY.put("light_purple", 'd');
		MINIMESSAGE_TO_LEGACY.put("yellow", 'e');
		MINIMESSAGE_TO_LEGACY.put("white", 'f');
		MINIMESSAGE_TO_LEGACY.put("obfuscated", 'k');
		MINIMESSAGE_TO_LEGACY.put("bold", 'l');
		MINIMESSAGE_TO_LEGACY.put("strikethrough", 'm');
		MINIMESSAGE_TO_LEGACY.put("underlined", 'n');
		MINIMESSAGE_TO_LEGACY.put("italic", 'o');
		MINIMESSAGE_TO_LEGACY.put("reset", 'r');
	}

	private TagUtils() {
	}

	public static @NotNull String normalizeColorFormatting(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		StringBuilder out = new StringBuilder(input.length());
		int i = 0;
		while (i < input.length()) {
			char c = input.charAt(i);
			if (c == '<') {
				int close = input.indexOf('>', i);
				if (close > i) {
					String token = input.substring(i + 1, close).trim();
					String replacement = convertMiniMessageToken(token);
					if (replacement != null) {
						out.append(replacement);
						i = close + 1;
						continue;
					}
				}
			}

			if (c == '§') {
				out.append('&');
			} else {
				out.append(c);
			}
			i++;
		}

		return out.toString();
	}

	public static @NotNull String stripFormatting(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		String normalized = normalizeColorFormatting(input);
		String stripped = LEGACY_HEX_PATTERN.matcher(normalized).replaceAll("");
		stripped = HEX_AMP_PATTERN.matcher(stripped).replaceAll("");
		stripped = HEX_SECTION_PATTERN.matcher(stripped).replaceAll("");
		stripped = LEGACY_COLOR_PATTERN.matcher(stripped).replaceAll("");
		stripped = MINIMESSAGE_PATTERN.matcher(stripped).replaceAll("");
		return stripped;
	}

	public static @NotNull String normalizeVisible(String input) {
		return stripFormatting(input).toLowerCase(Locale.ROOT);
	}

	public static int visibleLength(String input) {
		return stripFormatting(input).length();
	}

	public static @NotNull String firstLetters(String input, int maxLength) {
		if (input == null || input.isEmpty() || maxLength <= 0) {
			return "";
		}

		String plain = stripFormatting(input);
		StringBuilder out = new StringBuilder(Math.min(maxLength, plain.length()));
		for (int i = 0; i < plain.length() && out.length() < maxLength; i++) {
			char c = plain.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				out.append(Character.toUpperCase(c));
			}
		}
		return out.toString();
	}

	private static String convertMiniMessageToken(String token) {
		if (token == null || token.isEmpty()) {
			return null;
		}

		String lower = token.toLowerCase(Locale.ROOT);
		if (lower.startsWith("/")) {
			return "&r";
		}

		if (lower.startsWith("#") && lower.length() == HEX_COLOR_LENGTH) {
			String hex = lower.substring(1);
			if (hex.matches("[0-9a-f]{6}")) {
				return "&#" + hex;
			}
		}

		Character legacyCode = MINIMESSAGE_TO_LEGACY.get(lower);
		if (legacyCode != null) {
			return "&" + legacyCode;
		}

		return null;
	}
}

