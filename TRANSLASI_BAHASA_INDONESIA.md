# RINGKASAN TERJEMAHAN BAHASA INDONESIA - PARTIES PLUGIN

## Status: ✓ SELESAI

### File yang Diterjemahkan:
1. **Bukkit**: `bukkit/src/main/resources/bukkit/messages.yml` (658 baris)
2. **BungeeCord**: `bungeecord/src/main/resources/bungee/messages.yml` (658 baris)
3. **Velocity**: `velocity/src/main/resources/velocity/messages.yml` (658 baris)

### Cakupan Terjemahan:

#### 1. **PARTIES MESSAGES** (Umum)
- ✓ Update notifications
- ✓ Configuration messages
- ✓ Common messages (invalid command, not in party, dll)
- ✓ Permission messages
- ✓ Chat format messages

#### 2. **MAIN COMMANDS**
- ✓ Accept/Deny requests
- ✓ Chat toggle
- ✓ Guild create/delete
- ✓ Ignore/Info commands
- ✓ Invite/Kick players
- ✓ Leave guild
- ✓ Rank system
- ✓ Rename guild
- ✓ Spy functionality
- ✓ Version information

#### 3. **ADDITIONAL COMMANDS**
- ✓ Ask to join
- ✓ Claim management
- ✓ Color system
- ✓ Debug commands
- ✓ Description settings
- ✓ Experience system
- ✓ Follow leader
- ✓ Home teleport
- ✓ Join/Leave guild
- ✓ Guild list
- ✓ MOTD settings
- ✓ Mute notifications
- ✓ Nickname system
- ✓ Password settings
- ✓ Protection system
- ✓ Sethome
- ✓ Tag settings
- ✓ Teleport
- ✓ **TAX SYSTEM** (dengan %days%, %payer%, %status%)
- ✓ Vault/Economy

#### 4. **OTHER MESSAGES**
- ✓ Follow world notifications
- ✓ Fixed parties
- ✓ Join/Leave notifications

#### 5. **HELP MESSAGES**
- ✓ Command help pages
- ✓ Command descriptions
- ✓ Syntax information

### Fitur Khusus - TAX MESSAGES:

Pesan pajak guild telah diterjemahkan lengkap dengan fitur baru:

```yaml
tax:
  info: "Pajak guild jatuh tempo: &6%amount%&b. Hari tersisa: &e%days%&b (&e%time%&b). Status: %status%&b. Pembayar: &e%payer%"
  paid: "Anda membayar &6%amount%&a pajak guild"
  already-paid: "Siklus pajak ini sudah dibayar oleh &e%payer%&a. Jatuh tempo dalam &e%days%h &7(&e%time%&7)"
  failed: "Anda tidak memiliki cukup uang untuk membayar pajak guild [%amount%$]"
  no-economy: "Vault tidak diaktifkan, pembayaran pajak tidak tersedia"
  status-paid: "&aSudah Dibayar"
  status-unpaid: "&cBelum Dibayar"
  auto-delete-broadcast: "&cGuild Anda telah dihapus karena pajak tidak dibayar tepat waktu"
```

### Strategi Terjemahan:

1. **Istilah Teknis**: Dipertahankan dalam Bahasa Inggris ketika lebih cocok
   - `guild` → tetap "guild" (bukan "perserikatan")
   - `rank` → tetap "rank"
   - `experience` → tetap "experience"
   - `tag` → tetap "tag"
   - `password` → tetap "password"
   - `MOTD` → tetap "MOTD"

2. **Pesan Pengguna**: Diterjemahkan ke Bahasa Indonesia yang natural
   - Commands → Perintah
   - Players → Pemain
   - Members → Anggota
   - Invite → Undang
   - Kick → Tendang
   - Leave → Tinggalkan
   - dll.

3. **Konsistensi Lintas Platform**:
   - Bukkit, BungeeCord, dan Velocity memiliki pesan yang identik
   - Memastikan pengalaman pengguna yang konsisten di semua platform

### Keterangan Warna (Tetap):

Semua kode warna Minecraft tetap dipertahankan:
- `&a` = Hijau
- `&b` = Biru
- `&c` = Merah
- `&7` = Abu-abu
- `&e` = Kuning
- `&6` = Emas
- dll.

### Placeholders (Tetap):

Semua placeholder tetap berfungsi dengan benar:
- `%party%` = Nama guild
- `%player%` = Nama pemain
- `%amount%` = Jumlah uang
- `%time%` = Durasi waktu
- `%days%` = Jumlah hari (TAX)
- `%status%` = Status (TAX)
- `%payer%` = Nama pembayar (TAX)
- dll.

### Tidak Diterjemahkan:

1. **Komentar Dokumentasi**: Tetap dalam Bahasa Inggris untuk konsistensi dokumentasi
2. **Nama Plugin**: "Parties" tetap "Parties"
3. **URL & Link**: Tetap seperti aslinya

### Hasil Akhir:

✓ **Semua pesan plugin Parties telah diterjemahkan ke Bahasa Indonesia**
✓ **Istilah teknis yang cocok tetap menggunakan Bahasa Inggris**
✓ **Pesan tax system dengan fitur baru (%days%, %payer%, %status%) sudah tersedia**
✓ **Konsistensi antar platform (Bukkit, BungeeCord, Velocity) terjaga**
✓ **Semua placeholder dan kode warna tetap berfungsi dengan baik**

### Catatan untuk Admin:

Jika ingin mengganti kembali ke Bahasa Inggris, file backup ada di:
- `bukkit/src/main/resources/bukkit/messages_id.yml`

Untuk mengedit pesan lebih lanjut, edit langsung di file `.yml` sesuai struktur YAML.

---

**Selesai pada**: 19 April 2026

