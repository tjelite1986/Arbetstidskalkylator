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
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SettingsDao_Impl implements SettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Settings> __insertionAdapterOfSettings;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Settings> __updateAdapterOfSettings;

  private final SharedSQLiteStatement __preparedStmtOfClearSettings;

  public SettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSettings = new EntityInsertionAdapter<Settings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `settings` (`id`,`baseHourlyRate`,`taxPercentage`,`vacationPayPercentage`,`overtimeRates`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Settings entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getBaseHourlyRate());
        statement.bindDouble(3, entity.getTaxPercentage());
        statement.bindDouble(4, entity.getVacationPayPercentage());
        final String _tmp = __converters.fromOvertimeRateList(entity.getOvertimeRates());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
      }
    };
    this.__updateAdapterOfSettings = new EntityDeletionOrUpdateAdapter<Settings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `settings` SET `id` = ?,`baseHourlyRate` = ?,`taxPercentage` = ?,`vacationPayPercentage` = ?,`overtimeRates` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Settings entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getBaseHourlyRate());
        statement.bindDouble(3, entity.getTaxPercentage());
        statement.bindDouble(4, entity.getVacationPayPercentage());
        final String _tmp = __converters.fromOvertimeRateList(entity.getOvertimeRates());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfClearSettings = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM settings";
        return _query;
      }
    };
  }

  @Override
  public Object insertSettings(final Settings settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSettings.insert(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSettings(final Settings settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSettings.handle(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearSettings(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSettings.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearSettings.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Settings> getSettings() {
    final String _sql = "SELECT * FROM settings WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"settings"}, new Callable<Settings>() {
      @Override
      @Nullable
      public Settings call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBaseHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "baseHourlyRate");
          final int _cursorIndexOfTaxPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "taxPercentage");
          final int _cursorIndexOfVacationPayPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "vacationPayPercentage");
          final int _cursorIndexOfOvertimeRates = CursorUtil.getColumnIndexOrThrow(_cursor, "overtimeRates");
          final Settings _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpBaseHourlyRate;
            _tmpBaseHourlyRate = _cursor.getDouble(_cursorIndexOfBaseHourlyRate);
            final double _tmpTaxPercentage;
            _tmpTaxPercentage = _cursor.getDouble(_cursorIndexOfTaxPercentage);
            final double _tmpVacationPayPercentage;
            _tmpVacationPayPercentage = _cursor.getDouble(_cursorIndexOfVacationPayPercentage);
            final List<OvertimeRate> _tmpOvertimeRates;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfOvertimeRates)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfOvertimeRates);
            }
            _tmpOvertimeRates = __converters.toOvertimeRateList(_tmp);
            _result = new Settings(_tmpId,_tmpBaseHourlyRate,_tmpTaxPercentage,_tmpVacationPayPercentage,_tmpOvertimeRates);
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
  public Object getSettingsSync(final Continuation<? super Settings> $completion) {
    final String _sql = "SELECT * FROM settings WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Settings>() {
      @Override
      @Nullable
      public Settings call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBaseHourlyRate = CursorUtil.getColumnIndexOrThrow(_cursor, "baseHourlyRate");
          final int _cursorIndexOfTaxPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "taxPercentage");
          final int _cursorIndexOfVacationPayPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "vacationPayPercentage");
          final int _cursorIndexOfOvertimeRates = CursorUtil.getColumnIndexOrThrow(_cursor, "overtimeRates");
          final Settings _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpBaseHourlyRate;
            _tmpBaseHourlyRate = _cursor.getDouble(_cursorIndexOfBaseHourlyRate);
            final double _tmpTaxPercentage;
            _tmpTaxPercentage = _cursor.getDouble(_cursorIndexOfTaxPercentage);
            final double _tmpVacationPayPercentage;
            _tmpVacationPayPercentage = _cursor.getDouble(_cursorIndexOfVacationPayPercentage);
            final List<OvertimeRate> _tmpOvertimeRates;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfOvertimeRates)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfOvertimeRates);
            }
            _tmpOvertimeRates = __converters.toOvertimeRateList(_tmp);
            _result = new Settings(_tmpId,_tmpBaseHourlyRate,_tmpTaxPercentage,_tmpVacationPayPercentage,_tmpOvertimeRates);
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
