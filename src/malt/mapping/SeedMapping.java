/**
 * SeedMapping.java 
 * Copyright (C) 2015 Daniel H. Huson
 *
 * (Some files contain contributions from other authors, who are then mentioned separately.)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package malt.mapping;

import jloda.util.Basic;
import jloda.util.CanceledException;
import jloda.util.ProgressListener;
import jloda.util.ProgressPercentage;
import malt.data.ISequenceAccessor;
import malt.data.RefIndex2ClassId;
import malt.data.ReferencesDBBuilder;
import megan.classification.IdParser;

import java.io.File;
import java.io.IOException;

/**
 * Maintains mapping from Reference indices to Seed
 * Daniel Huson, 8.2014
 */
public class SeedMapping extends RefIndex2ClassId {
    private final static byte[] MAGIC_NUMBER = "MASeedV1.0.".getBytes();

    /**
     * construct a table
     *
     * @param maxIndex
     */
    public SeedMapping(int maxIndex) {
        super(maxIndex);
    }

    /**
     * compute the mapping for the given reference database
     *
     * @param referencesDB
     * @param progress
     */
    public static SeedMapping create(ISequenceAccessor referencesDB, IdParser classificationMapper, ProgressListener progress) throws CanceledException {
        SeedMapping mapping = new SeedMapping(referencesDB.getNumberOfSequences());

        progress.setMaximum(referencesDB.getNumberOfSequences());
        progress.setProgress(0);
        for (int i = 0; i < referencesDB.getNumberOfSequences(); i++) {
            String header = Basic.toString(referencesDB.getHeader(i));
            Integer classId = classificationMapper.getIdFromHeaderLine(header);
            if (classId != null) {
                mapping.put(i, classId);
                referencesDB.extendHeader(i, ReferencesDBBuilder.SEED_TAG, classId);
            }
            progress.incrementProgress();
        }
        if (progress instanceof ProgressPercentage)
            progress.close();

        return mapping;
    }

    /**
     * save to a stream and then close the stream
     *
     * @param file
     * @throws java.io.IOException
     */
    public void save(File file) throws IOException, CanceledException {
        super.save(file, MAGIC_NUMBER);
    }

    /**
     * construct from an existing file
     *
     * @param file
     * @throws java.io.IOException
     * @throws jloda.util.CanceledException
     */
    public SeedMapping(File file) throws IOException, CanceledException {
        super(file, MAGIC_NUMBER);
    }
}
