package com.yashkasera.watsights.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class WatSightsDatabase_Impl extends WatSightsDatabase {
  private volatile DatabaseDao _databaseDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `GROUPS` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `last_viewed` TEXT, `icon` INTEGER NOT NULL, `store_messages` INTEGER NOT NULL, `priority` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `MESSAGES` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `message` TEXT, `groupId` INTEGER NOT NULL, `personId` INTEGER NOT NULL, `timestamp` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ELITE` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `messageId` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `IMPORTANT` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `messageId` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `GROUP_MEMBERS` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` INTEGER NOT NULL, `personId` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `PEOPLE` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `elite` INTEGER NOT NULL, `spammer` INTEGER NOT NULL, `icon` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `PINNED` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `messageId` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `SPAM` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `messageId` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `MEDIA` (`name` TEXT, `type` INTEGER NOT NULL, `path` TEXT, `personId` INTEGER NOT NULL, `groupId` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isDeleted` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0efd7cb1675c09b4ac058a190dc0616e')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `GROUPS`");
        _db.execSQL("DROP TABLE IF EXISTS `MESSAGES`");
        _db.execSQL("DROP TABLE IF EXISTS `ELITE`");
        _db.execSQL("DROP TABLE IF EXISTS `IMPORTANT`");
        _db.execSQL("DROP TABLE IF EXISTS `GROUP_MEMBERS`");
        _db.execSQL("DROP TABLE IF EXISTS `PEOPLE`");
        _db.execSQL("DROP TABLE IF EXISTS `PINNED`");
        _db.execSQL("DROP TABLE IF EXISTS `SPAM`");
        _db.execSQL("DROP TABLE IF EXISTS `MEDIA`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsGROUPS = new HashMap<String, TableInfo.Column>(6);
        _columnsGROUPS.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGROUPS.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGROUPS.put("last_viewed", new TableInfo.Column("last_viewed", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGROUPS.put("icon", new TableInfo.Column("icon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGROUPS.put("store_messages", new TableInfo.Column("store_messages", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGROUPS.put("priority", new TableInfo.Column("priority", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGROUPS = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGROUPS = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGROUPS = new TableInfo("GROUPS", _columnsGROUPS, _foreignKeysGROUPS, _indicesGROUPS);
        final TableInfo _existingGROUPS = TableInfo.read(_db, "GROUPS");
        if (! _infoGROUPS.equals(_existingGROUPS)) {
          return new RoomOpenHelper.ValidationResult(false, "GROUPS(com.yashkasera.watsights.model.Group).\n"
                  + " Expected:\n" + _infoGROUPS + "\n"
                  + " Found:\n" + _existingGROUPS);
        }
        final HashMap<String, TableInfo.Column> _columnsMESSAGES = new HashMap<String, TableInfo.Column>(5);
        _columnsMESSAGES.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMESSAGES.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMESSAGES.put("groupId", new TableInfo.Column("groupId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMESSAGES.put("personId", new TableInfo.Column("personId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMESSAGES.put("timestamp", new TableInfo.Column("timestamp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMESSAGES = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMESSAGES = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMESSAGES = new TableInfo("MESSAGES", _columnsMESSAGES, _foreignKeysMESSAGES, _indicesMESSAGES);
        final TableInfo _existingMESSAGES = TableInfo.read(_db, "MESSAGES");
        if (! _infoMESSAGES.equals(_existingMESSAGES)) {
          return new RoomOpenHelper.ValidationResult(false, "MESSAGES(com.yashkasera.watsights.model.Message).\n"
                  + " Expected:\n" + _infoMESSAGES + "\n"
                  + " Found:\n" + _existingMESSAGES);
        }
        final HashMap<String, TableInfo.Column> _columnsELITE = new HashMap<String, TableInfo.Column>(2);
        _columnsELITE.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsELITE.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysELITE = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesELITE = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoELITE = new TableInfo("ELITE", _columnsELITE, _foreignKeysELITE, _indicesELITE);
        final TableInfo _existingELITE = TableInfo.read(_db, "ELITE");
        if (! _infoELITE.equals(_existingELITE)) {
          return new RoomOpenHelper.ValidationResult(false, "ELITE(com.yashkasera.watsights.model.EliteMessage).\n"
                  + " Expected:\n" + _infoELITE + "\n"
                  + " Found:\n" + _existingELITE);
        }
        final HashMap<String, TableInfo.Column> _columnsIMPORTANT = new HashMap<String, TableInfo.Column>(2);
        _columnsIMPORTANT.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIMPORTANT.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIMPORTANT = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesIMPORTANT = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIMPORTANT = new TableInfo("IMPORTANT", _columnsIMPORTANT, _foreignKeysIMPORTANT, _indicesIMPORTANT);
        final TableInfo _existingIMPORTANT = TableInfo.read(_db, "IMPORTANT");
        if (! _infoIMPORTANT.equals(_existingIMPORTANT)) {
          return new RoomOpenHelper.ValidationResult(false, "IMPORTANT(com.yashkasera.watsights.model.ImportantMessage).\n"
                  + " Expected:\n" + _infoIMPORTANT + "\n"
                  + " Found:\n" + _existingIMPORTANT);
        }
        final HashMap<String, TableInfo.Column> _columnsGROUPMEMBERS = new HashMap<String, TableInfo.Column>(3);
        _columnsGROUPMEMBERS.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGROUPMEMBERS.put("groupId", new TableInfo.Column("groupId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGROUPMEMBERS.put("personId", new TableInfo.Column("personId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGROUPMEMBERS = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGROUPMEMBERS = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGROUPMEMBERS = new TableInfo("GROUP_MEMBERS", _columnsGROUPMEMBERS, _foreignKeysGROUPMEMBERS, _indicesGROUPMEMBERS);
        final TableInfo _existingGROUPMEMBERS = TableInfo.read(_db, "GROUP_MEMBERS");
        if (! _infoGROUPMEMBERS.equals(_existingGROUPMEMBERS)) {
          return new RoomOpenHelper.ValidationResult(false, "GROUP_MEMBERS(com.yashkasera.watsights.model.GroupMember).\n"
                  + " Expected:\n" + _infoGROUPMEMBERS + "\n"
                  + " Found:\n" + _existingGROUPMEMBERS);
        }
        final HashMap<String, TableInfo.Column> _columnsPEOPLE = new HashMap<String, TableInfo.Column>(5);
        _columnsPEOPLE.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPEOPLE.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPEOPLE.put("elite", new TableInfo.Column("elite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPEOPLE.put("spammer", new TableInfo.Column("spammer", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPEOPLE.put("icon", new TableInfo.Column("icon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPEOPLE = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPEOPLE = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPEOPLE = new TableInfo("PEOPLE", _columnsPEOPLE, _foreignKeysPEOPLE, _indicesPEOPLE);
        final TableInfo _existingPEOPLE = TableInfo.read(_db, "PEOPLE");
        if (! _infoPEOPLE.equals(_existingPEOPLE)) {
          return new RoomOpenHelper.ValidationResult(false, "PEOPLE(com.yashkasera.watsights.model.People).\n"
                  + " Expected:\n" + _infoPEOPLE + "\n"
                  + " Found:\n" + _existingPEOPLE);
        }
        final HashMap<String, TableInfo.Column> _columnsPINNED = new HashMap<String, TableInfo.Column>(2);
        _columnsPINNED.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPINNED.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPINNED = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPINNED = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPINNED = new TableInfo("PINNED", _columnsPINNED, _foreignKeysPINNED, _indicesPINNED);
        final TableInfo _existingPINNED = TableInfo.read(_db, "PINNED");
        if (! _infoPINNED.equals(_existingPINNED)) {
          return new RoomOpenHelper.ValidationResult(false, "PINNED(com.yashkasera.watsights.model.PinnedMessage).\n"
                  + " Expected:\n" + _infoPINNED + "\n"
                  + " Found:\n" + _existingPINNED);
        }
        final HashMap<String, TableInfo.Column> _columnsSPAM = new HashMap<String, TableInfo.Column>(2);
        _columnsSPAM.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSPAM.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSPAM = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSPAM = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSPAM = new TableInfo("SPAM", _columnsSPAM, _foreignKeysSPAM, _indicesSPAM);
        final TableInfo _existingSPAM = TableInfo.read(_db, "SPAM");
        if (! _infoSPAM.equals(_existingSPAM)) {
          return new RoomOpenHelper.ValidationResult(false, "SPAM(com.yashkasera.watsights.model.SpamMessage).\n"
                  + " Expected:\n" + _infoSPAM + "\n"
                  + " Found:\n" + _existingSPAM);
        }
        final HashMap<String, TableInfo.Column> _columnsMEDIA = new HashMap<String, TableInfo.Column>(7);
        _columnsMEDIA.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMEDIA.put("type", new TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMEDIA.put("path", new TableInfo.Column("path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMEDIA.put("personId", new TableInfo.Column("personId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMEDIA.put("groupId", new TableInfo.Column("groupId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMEDIA.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMEDIA.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMEDIA = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMEDIA = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMEDIA = new TableInfo("MEDIA", _columnsMEDIA, _foreignKeysMEDIA, _indicesMEDIA);
        final TableInfo _existingMEDIA = TableInfo.read(_db, "MEDIA");
        if (! _infoMEDIA.equals(_existingMEDIA)) {
          return new RoomOpenHelper.ValidationResult(false, "MEDIA(com.yashkasera.watsights.model.Media).\n"
                  + " Expected:\n" + _infoMEDIA + "\n"
                  + " Found:\n" + _existingMEDIA);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0efd7cb1675c09b4ac058a190dc0616e", "749be412f6b9512771811a754c170b7a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "GROUPS","MESSAGES","ELITE","IMPORTANT","GROUP_MEMBERS","PEOPLE","PINNED","SPAM","MEDIA");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `GROUPS`");
      _db.execSQL("DELETE FROM `MESSAGES`");
      _db.execSQL("DELETE FROM `ELITE`");
      _db.execSQL("DELETE FROM `IMPORTANT`");
      _db.execSQL("DELETE FROM `GROUP_MEMBERS`");
      _db.execSQL("DELETE FROM `PEOPLE`");
      _db.execSQL("DELETE FROM `PINNED`");
      _db.execSQL("DELETE FROM `SPAM`");
      _db.execSQL("DELETE FROM `MEDIA`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public DatabaseDao databaseDao() {
    if (_databaseDao != null) {
      return _databaseDao;
    } else {
      synchronized(this) {
        if(_databaseDao == null) {
          _databaseDao = new DatabaseDao_Impl(this);
        }
        return _databaseDao;
      }
    }
  }
}
