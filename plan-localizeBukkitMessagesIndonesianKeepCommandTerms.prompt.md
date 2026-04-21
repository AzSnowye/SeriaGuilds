## Plan: Localize Bukkit Messages to Indonesian

Translate default Bukkit-facing messages into natural Indonesian while preserving command literals, placeholders, and formatting tokens exactly. Work from the default source file, mirror established locale structure, and keep all command-related terms unchanged to avoid breaking user guidance and command recognition consistency.

### Steps
1. Review source keys in [messages.yml](bukkit/src/main/resources/bukkit/messages.yml) for full default message scope.
2. Create Indonesian locale file at [messages_id_ID.yml](locales/bukkit/messages_id_ID.yml) matching original key structure.
3. Translate user-facing text to Indonesian, preserving placeholders and color/format tokens exactly.
4. Keep command terms unchanged (for example `/party`, subcommands, and command keywords) across all translated entries.
5. Compare with existing locale patterns in [locales/bukkit](locales/bukkit) to align tone and formatting conventions.

### Further Considerations
1. Which command words stay English: only slash commands, or also argument labels? Option A / Option B / Option C.
2. Prefer formal Indonesian style or casual community-server tone for system messages?

