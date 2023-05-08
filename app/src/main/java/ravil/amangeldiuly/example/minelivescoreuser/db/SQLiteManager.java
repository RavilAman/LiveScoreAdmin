package ravil.amangeldiuly.example.minelivescoreuser.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;

public class SQLiteManager extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "live_score_user_db";
    private String favouritesTable = "favourite_tournaments";
    private String topicsTable = "topics";

    private static SQLiteManager sqLiteManager;

    public SQLiteManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static SQLiteManager getInstance(Context context) {
        if (sqLiteManager == null) {
            sqLiteManager = new SQLiteManager(context);
        }
        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder createFavTourQuery = new StringBuilder();
        createFavTourQuery.append("create table ")
                .append(favouritesTable)
                .append(" ( ")
                .append("tournament_id integer primary key, ")  // todo: перенести в переменные названия колонок
                .append("tournament_name text, ")
                .append("tournament_logo text, ")
                .append("tournament_type text, ")
                .append("tournament_location text")
                .append(" );");

        sqLiteDatabase.execSQL(createFavTourQuery.toString());

        StringBuilder createTopicsTableQuery = new StringBuilder();
        createTopicsTableQuery.append("create table ")
                .append(topicsTable)
                .append(" ( ")
                .append("topic_id integer primary key autoincrement, ")
                .append("topic_name text")
                .append(" );");

        sqLiteDatabase.execSQL(createTopicsTableQuery.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @SuppressLint("Range")
    public List<TournamentDto> getAllTournaments() {
        List<TournamentDto> favouriteTournaments = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + favouritesTable, null);
        if (cursor.moveToFirst()) {
            do {
                TournamentDto tournamentDto = new TournamentDto();
                tournamentDto.setTournamentId((long) cursor.getInt(cursor.getColumnIndex("tournament_id")));
                tournamentDto.setTournamentName(cursor.getString(cursor.getColumnIndex("tournament_name")));
                tournamentDto.setTournamentLogo(cursor.getString(cursor.getColumnIndex("tournament_logo")));
                tournamentDto.setTournamentType(cursor.getString(cursor.getColumnIndex("tournament_type")));
                tournamentDto.setTournamentLocation(cursor.getString(cursor.getColumnIndex("tournament_location")));
                favouriteTournaments.add(tournamentDto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return favouriteTournaments;
    }

    public boolean tournamentPresents(int tournamentId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + favouritesTable + " where tournament_id = ?",
                new String[]{String.valueOf(tournamentId)});
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public void addTournamentToFavourites(TournamentDto tournamentDto) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tournament_id", tournamentDto.getTournamentId());
        contentValues.put("tournament_name", tournamentDto.getTournamentName());
        contentValues.put("tournament_logo", tournamentDto.getTournamentLogo());
        contentValues.put("tournament_type", tournamentDto.getTournamentType());
        contentValues.put("tournament_location", tournamentDto.getTournamentLocation());

        sqLiteDatabase.insert(favouritesTable, null, contentValues);
    }

    public void deleteTournamentFromFavourites(long tournamentId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(favouritesTable, "tournament_id = ?", new String[]{String.valueOf(tournamentId)});
    }

    @SuppressLint("Range")
    public List<String> getAllTopics() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<String> topicList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + topicsTable, null);
        if (cursor.moveToFirst()) {
            do {
                topicList.add(cursor.getString(cursor.getColumnIndex("topic_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return topicList;
    }

    public void addTopic(String topicName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("topic_name", topicName);

        sqLiteDatabase.insert(topicsTable, null, contentValues);
    }

    public void deleteTopic(String topicName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(topicsTable, "topic_name = ?", new String[]{topicName});
    }
}
