package com.yashkasera.watsights.database;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.yashkasera.watsights.model.EliteMessage;
import com.yashkasera.watsights.model.Group;
import com.yashkasera.watsights.model.GroupMember;
import com.yashkasera.watsights.model.ImportantMessage;
import com.yashkasera.watsights.model.Media;
import com.yashkasera.watsights.model.Message;
import com.yashkasera.watsights.model.MessageQuery;
import com.yashkasera.watsights.model.People;
import com.yashkasera.watsights.model.PinnedMessage;
import com.yashkasera.watsights.model.SpamMessage;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DatabaseDao_Impl implements DatabaseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Group> __insertionAdapterOfGroup;

  private final EntityInsertionAdapter<People> __insertionAdapterOfPeople;

  private final EntityInsertionAdapter<Message> __insertionAdapterOfMessage;

  private final EntityInsertionAdapter<Media> __insertionAdapterOfMedia;

  private final EntityInsertionAdapter<EliteMessage> __insertionAdapterOfEliteMessage;

  private final EntityInsertionAdapter<ImportantMessage> __insertionAdapterOfImportantMessage;

  private final EntityInsertionAdapter<SpamMessage> __insertionAdapterOfSpamMessage;

  private final EntityInsertionAdapter<PinnedMessage> __insertionAdapterOfPinnedMessage;

  private final EntityInsertionAdapter<GroupMember> __insertionAdapterOfGroupMember;

  private final EntityDeletionOrUpdateAdapter<Group> __updateAdapterOfGroup;

  private final EntityDeletionOrUpdateAdapter<People> __updateAdapterOfPeople;

  private final SharedSQLiteStatement __preparedStmtOfDeletePinned;

  private final SharedSQLiteStatement __preparedStmtOfResetElitePeople;

  private final SharedSQLiteStatement __preparedStmtOfResetSpammers;

  private final SharedSQLiteStatement __preparedStmtOfDeleteElite;

  private final SharedSQLiteStatement __preparedStmtOfDeleteGroups;

  private final SharedSQLiteStatement __preparedStmtOfDeleteGroupMembers;

  private final SharedSQLiteStatement __preparedStmtOfDeleteImportantMessages;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessages;

  private final SharedSQLiteStatement __preparedStmtOfDeletePeople;

  private final SharedSQLiteStatement __preparedStmtOfDeletePinnedMessages;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSpamMessages;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMedia;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMedia;

  public DatabaseDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGroup = new EntityInsertionAdapter<Group>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `GROUPS` (`id`,`name`,`last_viewed`,`icon`,`store_messages`,`priority`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Group value) {
        stmt.bindLong(1, value.id);
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getLast_viewed() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLast_viewed());
        }
        stmt.bindLong(4, value.getIcon());
        final int _tmp;
        _tmp = value.isStore_messages() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        stmt.bindLong(6, value.getPriority());
      }
    };
    this.__insertionAdapterOfPeople = new EntityInsertionAdapter<People>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `PEOPLE` (`id`,`name`,`elite`,`spammer`,`icon`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, People value) {
        stmt.bindLong(1, value.id);
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        final int _tmp;
        _tmp = value.isElite() ? 1 : 0;
        stmt.bindLong(3, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isSpammer() ? 1 : 0;
        stmt.bindLong(4, _tmp_1);
        stmt.bindLong(5, value.getIcon());
      }
    };
    this.__insertionAdapterOfMessage = new EntityInsertionAdapter<Message>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `MESSAGES` (`id`,`message`,`groupId`,`personId`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Message value) {
        stmt.bindLong(1, value.id);
        if (value.getMessage() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getMessage());
        }
        stmt.bindLong(3, value.getGroupId());
        stmt.bindLong(4, value.getPersonId());
        if (value.getTimestamp() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTimestamp());
        }
      }
    };
    this.__insertionAdapterOfMedia = new EntityInsertionAdapter<Media>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `MEDIA` (`name`,`type`,`path`,`personId`,`groupId`,`id`,`isDeleted`) VALUES (?,?,?,?,?,nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Media value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        stmt.bindLong(2, value.getType());
        if (value.getPath() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPath());
        }
        stmt.bindLong(4, value.getPersonId());
        stmt.bindLong(5, value.getGroupId());
        stmt.bindLong(6, value.getId());
        final int _tmp;
        _tmp = value.isDeleted() ? 1 : 0;
        stmt.bindLong(7, _tmp);
      }
    };
    this.__insertionAdapterOfEliteMessage = new EntityInsertionAdapter<EliteMessage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ELITE` (`id`,`messageId`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, EliteMessage value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.getMessageId());
      }
    };
    this.__insertionAdapterOfImportantMessage = new EntityInsertionAdapter<ImportantMessage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `IMPORTANT` (`id`,`messageId`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ImportantMessage value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.getMessageId());
      }
    };
    this.__insertionAdapterOfSpamMessage = new EntityInsertionAdapter<SpamMessage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `SPAM` (`id`,`messageId`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SpamMessage value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.getMessageId());
      }
    };
    this.__insertionAdapterOfPinnedMessage = new EntityInsertionAdapter<PinnedMessage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `PINNED` (`id`,`messageId`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PinnedMessage value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.getMessageId());
      }
    };
    this.__insertionAdapterOfGroupMember = new EntityInsertionAdapter<GroupMember>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `GROUP_MEMBERS` (`id`,`groupId`,`personId`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, GroupMember value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.getGroupId());
        stmt.bindLong(3, value.getPersonId());
      }
    };
    this.__updateAdapterOfGroup = new EntityDeletionOrUpdateAdapter<Group>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `GROUPS` SET `id` = ?,`name` = ?,`last_viewed` = ?,`icon` = ?,`store_messages` = ?,`priority` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Group value) {
        stmt.bindLong(1, value.id);
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getLast_viewed() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLast_viewed());
        }
        stmt.bindLong(4, value.getIcon());
        final int _tmp;
        _tmp = value.isStore_messages() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        stmt.bindLong(6, value.getPriority());
        stmt.bindLong(7, value.id);
      }
    };
    this.__updateAdapterOfPeople = new EntityDeletionOrUpdateAdapter<People>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `PEOPLE` SET `id` = ?,`name` = ?,`elite` = ?,`spammer` = ?,`icon` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, People value) {
        stmt.bindLong(1, value.id);
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        final int _tmp;
        _tmp = value.isElite() ? 1 : 0;
        stmt.bindLong(3, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isSpammer() ? 1 : 0;
        stmt.bindLong(4, _tmp_1);
        stmt.bindLong(5, value.getIcon());
        stmt.bindLong(6, value.id);
      }
    };
    this.__preparedStmtOfDeletePinned = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM PINNED WHERE messageId=?";
        return _query;
      }
    };
    this.__preparedStmtOfResetElitePeople = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE PEOPLE SET elite=0";
        return _query;
      }
    };
    this.__preparedStmtOfResetSpammers = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE PEOPLE SET spammer=0";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteElite = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ELITE";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteGroups = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM GROUPS";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteGroupMembers = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM GROUP_MEMBERS";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteImportantMessages = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM IMPORTANT";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessages = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM MESSAGES";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePeople = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM PEOPLE";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePinnedMessages = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM PINNED";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSpamMessages = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM SPAM";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMedia = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM MEDIA";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMedia = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE MEDIA SET path=?, isDeleted=1 WHERE path=?";
        return _query;
      }
    };
  }

  @Override
  public long insertGroup(final Group group) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfGroup.insertAndReturnId(group);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertPerson(final People person) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfPeople.insertAndReturnId(person);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertMessage(final Message message) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfMessage.insertAndReturnId(message);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMedia(final Media media) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMedia.insert(media);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertElite(final EliteMessage eliteMessage) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEliteMessage.insert(eliteMessage);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertImportant(final ImportantMessage importantMessage) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfImportantMessage.insert(importantMessage);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertSpam(final SpamMessage spamMessage) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSpamMessage.insert(spamMessage);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertPinned(final PinnedMessage pinnedMessage) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfPinnedMessage.insertAndReturnId(pinnedMessage);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMember(final GroupMember groupMember) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGroupMember.insert(groupMember);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateGroup(final Group groups) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfGroup.handle(groups);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updatePerson(final People person) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPeople.handle(person);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int deletePinned(final long messageId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePinned.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, messageId);
    __db.beginTransaction();
    try {
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeletePinned.release(_stmt);
    }
  }

  @Override
  public void resetElitePeople() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfResetElitePeople.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfResetElitePeople.release(_stmt);
    }
  }

  @Override
  public void resetSpammers() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfResetSpammers.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfResetSpammers.release(_stmt);
    }
  }

  @Override
  public void deleteElite() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteElite.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteElite.release(_stmt);
    }
  }

  @Override
  public void deleteGroups() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteGroups.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteGroups.release(_stmt);
    }
  }

  @Override
  public void deleteGroupMembers() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteGroupMembers.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteGroupMembers.release(_stmt);
    }
  }

  @Override
  public void deleteImportantMessages() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteImportantMessages.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteImportantMessages.release(_stmt);
    }
  }

  @Override
  public void deleteMessages() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessages.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteMessages.release(_stmt);
    }
  }

  @Override
  public void deletePeople() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePeople.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeletePeople.release(_stmt);
    }
  }

  @Override
  public void deletePinnedMessages() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePinnedMessages.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeletePinnedMessages.release(_stmt);
    }
  }

  @Override
  public void deleteSpamMessages() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSpamMessages.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteSpamMessages.release(_stmt);
    }
  }

  @Override
  public void deleteMedia() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMedia.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteMedia.release(_stmt);
    }
  }

  @Override
  public void updateMedia(final String prevPath, final String newPath) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMedia.acquire();
    int _argIndex = 1;
    if (newPath == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, newPath);
    }
    _argIndex = 2;
    if (prevPath == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, prevPath);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateMedia.release(_stmt);
    }
  }

  @Override
  public GroupMember isMember(final long groupId, final long personId) {
    final String _sql = "SELECT * FROM GROUP_MEMBERS WHERE groupId=? AND personId=? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, personId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
      final GroupMember _result;
      if(_cursor.moveToFirst()) {
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final long _tmpPersonId;
        _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
        _result = new GroupMember(_tmpGroupId,_tmpPersonId);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Group>> getGroups() {
    final String _sql = "SELECT * FROM GROUPS ORDER BY priority DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"GROUPS"}, false, new Callable<List<Group>>() {
      @Override
      public List<Group> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastViewed = CursorUtil.getColumnIndexOrThrow(_cursor, "last_viewed");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfStoreMessages = CursorUtil.getColumnIndexOrThrow(_cursor, "store_messages");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final List<Group> _result = new ArrayList<Group>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Group _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLast_viewed;
            _tmpLast_viewed = _cursor.getString(_cursorIndexOfLastViewed);
            final int _tmpIcon;
            _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
            final boolean _tmpStore_messages;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfStoreMessages);
            _tmpStore_messages = _tmp != 0;
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            _item = new Group(_tmpName,_tmpLast_viewed,_tmpIcon,_tmpStore_messages,_tmpPriority);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Group> getGroup(final long groupId) {
    final String _sql = "SELECT * FROM GROUPS WHERE id=? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    return __db.getInvalidationTracker().createLiveData(new String[]{"GROUPS"}, false, new Callable<Group>() {
      @Override
      public Group call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastViewed = CursorUtil.getColumnIndexOrThrow(_cursor, "last_viewed");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfStoreMessages = CursorUtil.getColumnIndexOrThrow(_cursor, "store_messages");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final Group _result;
          if(_cursor.moveToFirst()) {
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLast_viewed;
            _tmpLast_viewed = _cursor.getString(_cursorIndexOfLastViewed);
            final int _tmpIcon;
            _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
            final boolean _tmpStore_messages;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfStoreMessages);
            _tmpStore_messages = _tmp != 0;
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            _result = new Group(_tmpName,_tmpLast_viewed,_tmpIcon,_tmpStore_messages,_tmpPriority);
            _result.id = _cursor.getLong(_cursorIndexOfId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Group getGroupByName(final String groupName) {
    final String _sql = "SELECT * FROM GROUPS WHERE NAME = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (groupName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, groupName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfLastViewed = CursorUtil.getColumnIndexOrThrow(_cursor, "last_viewed");
      final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
      final int _cursorIndexOfStoreMessages = CursorUtil.getColumnIndexOrThrow(_cursor, "store_messages");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final Group _result;
      if(_cursor.moveToFirst()) {
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpLast_viewed;
        _tmpLast_viewed = _cursor.getString(_cursorIndexOfLastViewed);
        final int _tmpIcon;
        _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
        final boolean _tmpStore_messages;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfStoreMessages);
        _tmpStore_messages = _tmp != 0;
        final int _tmpPriority;
        _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
        _result = new Group(_tmpName,_tmpLast_viewed,_tmpIcon,_tmpStore_messages,_tmpPriority);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public People getPersonByName(final String personName) {
    final String _sql = "SELECT * FROM PEOPLE WHERE NAME = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (personName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, personName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
      final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
      final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
      final People _result;
      if(_cursor.moveToFirst()) {
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final boolean _tmpElite;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfElite);
        _tmpElite = _tmp != 0;
        final boolean _tmpSpammer;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
        _tmpSpammer = _tmp_1 != 0;
        final int _tmpIcon;
        _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
        _result = new People(_tmpName,_tmpElite,_tmpSpammer,_tmpIcon);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public PinnedMessage isPinned(final long messageId) {
    final String _sql = "SELECT * FROM PINNED WHERE messageId=? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, messageId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
      final PinnedMessage _result;
      if(_cursor.moveToFirst()) {
        final long _tmpMessageId;
        _tmpMessageId = _cursor.getLong(_cursorIndexOfMessageId);
        _result = new PinnedMessage(_tmpMessageId);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Message>> getMessages() {
    final String _sql = "SELECT * FROM MESSAGES";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MESSAGES"}, false, new Callable<List<Message>>() {
      @Override
      public List<Message> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Message> _result = new ArrayList<Message>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Message _item;
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final long _tmpGroupId;
            _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item = new Message(_tmpMessage,_tmpGroupId,_tmpPersonId,_tmpTimestamp);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<MessageQuery>> getGroupMessages(final long groupId) {
    final String _sql = "SELECT MESSAGES.id,message,PEOPLE.name personName,GROUPS.name groupName,elite,spammer,timestamp FROM MESSAGES \n"
            + "LEFT JOIN PEOPLE ON MESSAGES.personId = PEOPLE.id\n"
            + "LEFT JOIN GROUPS ON MESSAGES.groupId = GROUPS.id\n"
            + "WHERE groupId = ?\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MESSAGES","PEOPLE","GROUPS"}, false, new Callable<List<MessageQuery>>() {
      @Override
      public List<MessageQuery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MessageQuery> _result = new ArrayList<MessageQuery>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MessageQuery _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item = new MessageQuery(_tmpId,_tmpMessage,_tmpPersonName,_tmpGroupName,_tmpElite,_tmpSpammer,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<MessageQuery>> getPersonMessages(final long personId) {
    final String _sql = "SELECT MESSAGES.id,message,PEOPLE.name personName,GROUPS.name groupName,elite,spammer,timestamp FROM MESSAGES \n"
            + "LEFT JOIN ELITE ON ELITE.messageId = MESSAGES.id \n"
            + "LEFT JOIN GROUPS ON GROUPS.id = MESSAGES.groupId\n"
            + "LEFT JOIN PEOPLE ON MESSAGES.personId = PEOPLE.id\n"
            + "WHERE personId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MESSAGES","ELITE","GROUPS","PEOPLE"}, false, new Callable<List<MessageQuery>>() {
      @Override
      public List<MessageQuery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MessageQuery> _result = new ArrayList<MessageQuery>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MessageQuery _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item = new MessageQuery(_tmpId,_tmpMessage,_tmpPersonName,_tmpGroupName,_tmpElite,_tmpSpammer,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<MessageQuery>> getEliteMessages() {
    final String _sql = "SELECT MESSAGES.id,message,PEOPLE.name personName,GROUPS.name groupName,elite,spammer,timestamp FROM MESSAGES \n"
            + "INNER JOIN ELITE ON MESSAGES.id = ELITE.messageId\n"
            + "LEFT JOIN PEOPLE ON MESSAGES.personId = PEOPLE.id\n"
            + "LEFT JOIN GROUPS ON MESSAGES.groupId = GROUPS.id\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MESSAGES","ELITE","PEOPLE","GROUPS"}, false, new Callable<List<MessageQuery>>() {
      @Override
      public List<MessageQuery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MessageQuery> _result = new ArrayList<MessageQuery>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MessageQuery _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item = new MessageQuery(_tmpId,_tmpMessage,_tmpPersonName,_tmpGroupName,_tmpElite,_tmpSpammer,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<People>> getElitePeople() {
    final String _sql = "SELECT * FROM People WHERE elite=1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"People"}, false, new Callable<List<People>>() {
      @Override
      public List<People> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final List<People> _result = new ArrayList<People>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final People _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final int _tmpIcon;
            _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
            _item = new People(_tmpName,_tmpElite,_tmpSpammer,_tmpIcon);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<People>> getPeople() {
    final String _sql = "SELECT * FROM People";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"People"}, false, new Callable<List<People>>() {
      @Override
      public List<People> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final List<People> _result = new ArrayList<People>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final People _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final int _tmpIcon;
            _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
            _item = new People(_tmpName,_tmpElite,_tmpSpammer,_tmpIcon);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<People>> getSpammers() {
    final String _sql = "SELECT * FROM People WHERE spammer=1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"People"}, false, new Callable<List<People>>() {
      @Override
      public List<People> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final List<People> _result = new ArrayList<People>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final People _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final int _tmpIcon;
            _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
            _item = new People(_tmpName,_tmpElite,_tmpSpammer,_tmpIcon);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Media>> getDeletedMedia() {
    final String _sql = "SELECT * FROM MEDIA WHERE isDeleted=1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MEDIA"}, false, new Callable<List<Media>>() {
      @Override
      public List<Media> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<Media> _result = new ArrayList<Media>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Media _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final String _tmpPath;
            _tmpPath = _cursor.getString(_cursorIndexOfPath);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpGroupId;
            _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            _item = new Media(_tmpName,_tmpType,_tmpPath,_tmpPersonId,_tmpGroupId,_tmpIsDeleted);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Media>> getMedia() {
    final String _sql = "SELECT * FROM MEDIA";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MEDIA"}, false, new Callable<List<Media>>() {
      @Override
      public List<Media> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<Media> _result = new ArrayList<Media>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Media _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final String _tmpPath;
            _tmpPath = _cursor.getString(_cursorIndexOfPath);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpGroupId;
            _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            _item = new Media(_tmpName,_tmpType,_tmpPath,_tmpPersonId,_tmpGroupId,_tmpIsDeleted);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Media> getMediaByPath(final String path) {
    final String _sql = "SELECT * FROM MEDIA WHERE path=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (path == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, path);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"MEDIA"}, false, new Callable<Media>() {
      @Override
      public Media call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final Media _result;
          if(_cursor.moveToFirst()) {
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final String _tmpPath;
            _tmpPath = _cursor.getString(_cursorIndexOfPath);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpGroupId;
            _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            _result = new Media(_tmpName,_tmpType,_tmpPath,_tmpPersonId,_tmpGroupId,_tmpIsDeleted);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result.setId(_tmpId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<People>> getMembers(final long groupId) {
    final String _sql = "SELECT PEOPLE.id,PEOPLE.name,PEOPLE.elite,PEOPLE.spammer,PEOPLE.icon FROM GROUP_MEMBERS\n"
            + "INNER JOIN PEOPLE ON PEOPLE.id=GROUP_MEMBERS.personId\n"
            + "WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    return __db.getInvalidationTracker().createLiveData(new String[]{"GROUP_MEMBERS","PEOPLE"}, false, new Callable<List<People>>() {
      @Override
      public List<People> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final List<People> _result = new ArrayList<People>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final People _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final int _tmpIcon;
            _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
            _item = new People(_tmpName,_tmpElite,_tmpSpammer,_tmpIcon);
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Message getMessage(final long messageId) {
    final String _sql = "SELECT * FROM MESSAGES WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, messageId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final Message _result;
      if(_cursor.moveToFirst()) {
        final String _tmpMessage;
        _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final long _tmpPersonId;
        _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
        final String _tmpTimestamp;
        _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
        _result = new Message(_tmpMessage,_tmpGroupId,_tmpPersonId,_tmpTimestamp);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public People getPerson(final long personId) {
    final String _sql = "SELECT * FROM PEOPLE WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
      final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
      final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
      final People _result;
      if(_cursor.moveToFirst()) {
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final boolean _tmpElite;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfElite);
        _tmpElite = _tmp != 0;
        final boolean _tmpSpammer;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
        _tmpSpammer = _tmp_1 != 0;
        final int _tmpIcon;
        _tmpIcon = _cursor.getInt(_cursorIndexOfIcon);
        _result = new People(_tmpName,_tmpElite,_tmpSpammer,_tmpIcon);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<MessageQuery>> getSpamMessages() {
    final String _sql = "SELECT MESSAGES.id,message,PEOPLE.name personName,GROUPS.name groupName,elite,spammer,timestamp FROM MESSAGES \n"
            + "INNER JOIN SPAM ON MESSAGES.id = SPAM.messageId\n"
            + "LEFT JOIN PEOPLE ON MESSAGES.personId = PEOPLE.id\n"
            + "LEFT JOIN GROUPS ON MESSAGES.groupId = GROUPS.id\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MESSAGES","SPAM","PEOPLE","GROUPS"}, false, new Callable<List<MessageQuery>>() {
      @Override
      public List<MessageQuery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MessageQuery> _result = new ArrayList<MessageQuery>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MessageQuery _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item = new MessageQuery(_tmpId,_tmpMessage,_tmpPersonName,_tmpGroupName,_tmpElite,_tmpSpammer,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<MessageQuery>> getPinnedMessages() {
    final String _sql = "SELECT MESSAGES.id,message,PEOPLE.name personName,GROUPS.name groupName,elite,spammer,timestamp FROM MESSAGES \n"
            + "INNER JOIN PINNED ON MESSAGES.id = PINNED.messageId\n"
            + "LEFT JOIN PEOPLE ON MESSAGES.personId = PEOPLE.id\n"
            + "LEFT JOIN GROUPS ON MESSAGES.groupId = GROUPS.id\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MESSAGES","PINNED","PEOPLE","GROUPS"}, false, new Callable<List<MessageQuery>>() {
      @Override
      public List<MessageQuery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MessageQuery> _result = new ArrayList<MessageQuery>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MessageQuery _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item = new MessageQuery(_tmpId,_tmpMessage,_tmpPersonName,_tmpGroupName,_tmpElite,_tmpSpammer,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<MessageQuery>> getImportantMessages() {
    final String _sql = "SELECT MESSAGES.id, message,PEOPLE.name personName,GROUPS.name groupName,elite,spammer,timestamp FROM MESSAGES \n"
            + "INNER JOIN IMPORTANT ON MESSAGES.id = IMPORTANT.messageId\n"
            + "LEFT JOIN PEOPLE ON MESSAGES.personId = PEOPLE.id\n"
            + "LEFT JOIN GROUPS ON MESSAGES.groupId = GROUPS.id\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"MESSAGES","IMPORTANT","PEOPLE","GROUPS"}, false, new Callable<List<MessageQuery>>() {
      @Override
      public List<MessageQuery> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfElite = CursorUtil.getColumnIndexOrThrow(_cursor, "elite");
          final int _cursorIndexOfSpammer = CursorUtil.getColumnIndexOrThrow(_cursor, "spammer");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MessageQuery> _result = new ArrayList<MessageQuery>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MessageQuery _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final boolean _tmpElite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfElite);
            _tmpElite = _tmp != 0;
            final boolean _tmpSpammer;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSpammer);
            _tmpSpammer = _tmp_1 != 0;
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            _item = new MessageQuery(_tmpId,_tmpMessage,_tmpPersonName,_tmpGroupName,_tmpElite,_tmpSpammer,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public String getGroupLastMessage(final long groupId) {
    final String _sql = "SELECT message FROM MESSAGES \n"
            + "LEFT JOIN GROUPS ON  GROUPS.id =  MESSAGES.groupId\n"
            + "WHERE groupId=? ORDER BY timestamp DESC LIMIT 1;";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getString(0);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public String getPersonLastMessage(final long personId) {
    final String _sql = "SELECT message FROM MESSAGES \n"
            + "INNER JOIN PEOPLE ON  PEOPLE.id =  MESSAGES.personId\n"
            + "WHERE groupId IS NULL AND personId=?  ORDER BY timestamp DESC LIMIT 1;";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getString(0);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
