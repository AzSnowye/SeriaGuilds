package com.alessiodp.parties.common.storage.sql.dao.parties;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface H2PartiesDao extends PartiesDao {
	String QUERY_UPDATE = "MERGE INTO `<prefix>parties` (`id`, `name`, `tag`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`, `isopen`, `created_at`, `tax_last_paid`, `tax_last_payer`)" +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	@Override
	@SqlUpdate(QUERY_UPDATE)
	void update(String id, String name, String tag, String leader, String description, String motd, String color, int kills, String password, String home, boolean protection, double experience, boolean follow, Boolean isopen, long createdAt, long taxLastPaid, String taxLastPayer);
}
