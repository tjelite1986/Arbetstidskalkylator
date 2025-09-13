package se.thomas.arbetstidskalkylator.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TimeEntryDao_Impl implements TimeEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TimeEntry> __insertionAdapterOfTimeEntry;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<TimeEntry> __deletionAdapterOfTimeEntry;

  private final EntityDeletionOrUpdateAdapter<TimeEntry> __updateAdapterOfTimeEntry;

  public TimeEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTimeEntry = new EntityInsertionAdapter<TimeEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `time_entries` (`id`,`startTime`,`endTime`,`hourlyRate`,`description`,`isBreakDeducted`,`breakMinutes`,`customBreakStart`,`customBreakEnd`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TimeEntry entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.dateToTimestamp(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp_1);
        }
        statement.bindDouble(4, entity.getHourlyRate());
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        final int _tmp_2 = entity.isBreakDeducted() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
        statement.bindLong(7, entity.getBreakMinutes());
        final Long _tmp_3 = __converters.dateToTimestamp(entity.getCustomBreakStart());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_3);
        }
        final Long _tmp_4 = __converters.dateToTimestamp(entity.getCustomBreakEnd());
        if (_tmp_4 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_4);
        }
      }
    };
    this.__deletionAdapterOfTimeEntry = new EntityDeletionOrUpdateAdapter<TimeEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `time_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TimeEntry entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTimeEntry = new EntityDeletionOrUpdateAdapter<TimeEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `time_entries` SET `id` = ?,`startTime` = ?,`endTime` = ?,`hourlyRate` = ?,`description` = ?,`isBreakDeducted` = ?,`breakMinutes` = ?,`customBreakStart` = ?,`customBreakEnd` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TimeEntry entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.dateToTimestamp(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp_1);
        }
        statement.bindDouble(4, entity.getHourlyRate());
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        final int _tmp_2 = entity.isBreakDeducted() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
        statement.bindLong(7, entity.getBreakMinutes());
        final Long _tmp_3 = __converters.dateToTimestamp(entity.getCustomBreakStart());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_3);
        }
        final Long _tmp_4 = __converters.dateToTimestamp(entity.getCustomBreakEnd());
        if (_tmp_4 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_4);
        }
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insertTimeEntry(final TimeEntry timeEntry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTimeEntry.insertAndReturnId(timeEntry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTimeEntry(final TimeEntry timeEntry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTimeEntry.handle(timeEntry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTimeEntry(final TimeEntry timeEntry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTimeEntry.handle(timeEntry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TimeEntry>> getAllTimeEntries() {
    final String _sql = "SELECT * FROM time_entries ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"time_entries"}, new Callable<List<TimeEntry>>() {
      @Override
      @NonNull
      public List<TimeEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsBreakDeducted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBreakDeducted");
          final int _cursorIndexOfBreakMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "breakMinutes");
          final int _cursorIndexOfCustomBreakStart = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakStart");
          final int _cursorIndexOfCustomBreakEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakEnd");
          final List<TimeEntry> _result = new ArrayList<TimeEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TimeEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Date _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp);
            final Date _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_1);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsBreakDeducted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsBreakDeducted);
            _tmpIsBreakDeducted = _tmp_2 != 0;
            final int _tmpBreakMinutes;
            _tmpBreakMinutes = _cursor.getInt(_cursorIndexOfBreakMinutes);
            final Date _tmpCustomBreakStart;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCustomBreakStart)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfCustomBreakStart);
            }
            _tmpCustomBreakStart = __converters.fromTimestamp(_tmp_3);
            final Date _tmpCustomBreakEnd;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCustomBreakEnd)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCustomBreakEnd);
            }
            _tmpCustomBreakEnd = __converters.fromTimestamp(_tmp_4);
            _item = new TimeEntry(_tmpId,_tmpStartTime,_tmpEndTime,_tmpHourlyRate,_tmpDescription,_tmpIsBreakDeducted,_tmpBreakMinutes,_tmpCustomBreakStart,_tmpCustomBreakEnd);
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
  public Flow<List<TimeEntry>> getTimeEntriesByDateRange(final Date startDate, final Date endDate) {
    final String _sql = "SELECT * FROM time_entries WHERE startTime BETWEEN ? AND ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = __converters.dateToTimestamp(startDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = __converters.dateToTimestamp(endDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"time_entries"}, new Callable<List<TimeEntry>>() {
      @Override
      @NonNull
      public List<TimeEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsBreakDeducted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBreakDeducted");
          final int _cursorIndexOfBreakMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "breakMinutes");
          final int _cursorIndexOfCustomBreakStart = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakStart");
          final int _cursorIndexOfCustomBreakEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakEnd");
          final List<TimeEntry> _result = new ArrayList<TimeEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TimeEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Date _tmpStartTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp_2);
            final Date _tmpEndTime;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_3);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsBreakDeducted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsBreakDeducted);
            _tmpIsBreakDeducted = _tmp_4 != 0;
            final int _tmpBreakMinutes;
            _tmpBreakMinutes = _cursor.getInt(_cursorIndexOfBreakMinutes);
            final Date _tmpCustomBreakStart;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfCustomBreakStart)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfCustomBreakStart);
            }
            _tmpCustomBreakStart = __converters.fromTimestamp(_tmp_5);
            final Date _tmpCustomBreakEnd;
            final Long _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCustomBreakEnd)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getLong(_cursorIndexOfCustomBreakEnd);
            }
            _tmpCustomBreakEnd = __converters.fromTimestamp(_tmp_6);
            _item = new TimeEntry(_tmpId,_tmpStartTime,_tmpEndTime,_tmpHourlyRate,_tmpDescription,_tmpIsBreakDeducted,_tmpBreakMinutes,_tmpCustomBreakStart,_tmpCustomBreakEnd);
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
  public Object getTimeEntriesByDateRangeSync(final Date startDate, final Date endDate,
      final Continuation<? super List<TimeEntry>> $completion) {
    final String _sql = "SELECT * FROM time_entries WHERE startTime BETWEEN ? AND ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = __converters.dateToTimestamp(startDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = __converters.dateToTimestamp(endDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TimeEntry>>() {
      @Override
      @NonNull
      public List<TimeEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsBreakDeducted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBreakDeducted");
          final int _cursorIndexOfBreakMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "breakMinutes");
          final int _cursorIndexOfCustomBreakStart = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakStart");
          final int _cursorIndexOfCustomBreakEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakEnd");
          final List<TimeEntry> _result = new ArrayList<TimeEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TimeEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Date _tmpStartTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp_2);
            final Date _tmpEndTime;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_3);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsBreakDeducted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsBreakDeducted);
            _tmpIsBreakDeducted = _tmp_4 != 0;
            final int _tmpBreakMinutes;
            _tmpBreakMinutes = _cursor.getInt(_cursorIndexOfBreakMinutes);
            final Date _tmpCustomBreakStart;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfCustomBreakStart)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfCustomBreakStart);
            }
            _tmpCustomBreakStart = __converters.fromTimestamp(_tmp_5);
            final Date _tmpCustomBreakEnd;
            final Long _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCustomBreakEnd)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getLong(_cursorIndexOfCustomBreakEnd);
            }
            _tmpCustomBreakEnd = __converters.fromTimestamp(_tmp_6);
            _item = new TimeEntry(_tmpId,_tmpStartTime,_tmpEndTime,_tmpHourlyRate,_tmpDescription,_tmpIsBreakDeducted,_tmpBreakMinutes,_tmpCustomBreakStart,_tmpCustomBreakEnd);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTimeEntryById(final long id, final Continuation<? super TimeEntry> $completion) {
    final String _sql = "SELECT * FROM time_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TimeEntry>() {
      @Override
      @Nullable
      public TimeEntry call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "hourlyRate");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsBreakDeducted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBreakDeducted");
          final int _cursorIndexOfBreakMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "breakMinutes");
          final int _cursorIndexOfCustomBreakStart = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakStart");
          final int _cursorIndexOfCustomBreakEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "customBreakEnd");
          final TimeEntry _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Date _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp);
            final Date _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_1);
            final double _tmpHourlyRate;
            _tmpHourlyRate = _cursor.getDouble(_cursorIndexOfHourlyRate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpIsBreakDeducted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsBreakDeducted);
            _tmpIsBreakDeducted = _tmp_2 != 0;
            final int _tmpBreakMinutes;
            _tmpBreakMinutes = _cursor.getInt(_cursorIndexOfBreakMinutes);
            final Date _tmpCustomBreakStart;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCustomBreakStart)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfCustomBreakStart);
            }
            _tmpCustomBreakStart = __converters.fromTimestamp(_tmp_3);
            final Date _tmpCustomBreakEnd;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCustomBreakEnd)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCustomBreakEnd);
            }
            _tmpCustomBreakEnd = __converters.fromTimestamp(_tmp_4);
            _result = new TimeEntry(_tmpId,_tmpStartTime,_tmpEndTime,_tmpHourlyRate,_tmpDescription,_tmpIsBreakDeducted,_tmpBreakMinutes,_tmpCustomBreakStart,_tmpCustomBreakEnd);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
