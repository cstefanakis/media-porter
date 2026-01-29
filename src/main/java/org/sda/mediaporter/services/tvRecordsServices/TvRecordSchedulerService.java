package org.sda.mediaporter.services.tvRecordsServices;

public interface TvRecordSchedulerService {
    void copyMoviesFromTvRecordSources();
    void copyTvShowsFromTvRecordSources();
    void deleteOldTvRecords();
}
