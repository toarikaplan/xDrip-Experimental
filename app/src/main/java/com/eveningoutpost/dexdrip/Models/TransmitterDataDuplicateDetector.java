package com.eveningoutpost.dexdrip.Models;

/**
 * Strategy pattern to allow different (or multiple) sources to have different strategies to detect duplicates.
 *
 * @see com.eveningoutpost.dexdrip.Models.TransmitterData#create(byte[], int, long, android.content.Context)
 */
public class TransmitterDataDuplicateDetector {
    public boolean isConsideredDuplicate(TransmitterData lastData, TransmitterData newData) {
        if(lastData == null)
            return false;

        return lastData.raw_data == newData.raw_data && Math.abs(lastData.timestamp - newData.timestamp ) < (10000);
    }
}
