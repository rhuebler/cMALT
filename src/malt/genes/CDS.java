/*
 *  Copyright (C) 2015 Daniel H. Huson
 *
 *  (Some files contain contributions from other authors, who are then mentioned separately.)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package malt.genes;

import jloda.util.*;
import megan.classification.util.TaggedValueIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * simple CDS annotation of a segment of DNA
 * Daniel Huson, 12.2017
 */
public class CDS {
    private String dnaId;
    private int start;
    private int end;
    private boolean reverse;
    private String proteinId;

    /**
     * constructor
     */
    public CDS() {
    }

    public void setDnaId(String dnaId) {
        this.dnaId = dnaId;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }

    public String getDnaId() {
        return dnaId;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isReverse() {
        return reverse;
    }

    public String getProteinId() {
        return proteinId;
    }

    /**
     * parse GFF files to extract CDS
     *
     * @param inputFiles
     * @return list of CDS items
     * @throws IOException, CanceledException
     */
    public static Collection<CDS> parseGFFforCDS(Collection<String> inputFiles, ProgressListener progress) throws IOException, CanceledException {
        progress.setMaximum(inputFiles.size());
        progress.setProgress(0);
        final ArrayList<CDS> list = new ArrayList<>();
        for (String fileName : inputFiles) {
            progress.setTasks("Loading GFF files", fileName);

            try (FileInputIterator it = new FileInputIterator(fileName)) {
                if (it.hasNext()) {
                    {
                        final String aLine = it.next();
                        if (aLine.startsWith(">")) {
                            final TaggedValueIterator vit = new TaggedValueIterator(false, true, "ref|");
                            vit.restart(aLine);
                            if (!vit.hasNext())
                                throw new IOException("Can't find reference accession in file: '" + fileName + "', header line: '" + aLine + "'");
                            final String dnaAccession = vit.getFirst();
                            list.addAll(parseSimpleGFFforCDS(it, dnaAccession));
                        } else if (aLine.startsWith("##gff-version 3")) {
                            list.addAll(parseGFF3forCDS(it));
                        }
                    }
                }
            }
            progress.incrementProgress();
        }
        if (progress instanceof ProgressPercentage)
            ((ProgressPercentage) progress).reportTaskCompleted();
        return list;
    }

    /**
     * parse one line GFF3 format
     *
     * @param iterator
     * @return annotations
     */
    private static Collection<CDS> parseGFF3forCDS(Iterator<String> iterator) {
        final TaggedValueIterator dnaAccessionIterator = new TaggedValueIterator(true, true);
        final TaggedValueIterator proteinAccessionIterator = new TaggedValueIterator(false, true, "protein_id=", "name=", "sequence:RefSeq:");

        final ArrayList<CDS> list = new ArrayList<>(100000);
        while (iterator.hasNext()) {
            final String aLine = iterator.next();
            final String[] tokens = Basic.split(aLine, '\t');
            if (tokens.length >= 9 && tokens[2].equals("CDS")) {
                final CDS cds = new CDS();
                dnaAccessionIterator.restart(tokens[0]);
                if (dnaAccessionIterator.hasNext()) {
                    cds.setDnaId(dnaAccessionIterator.getFirst());
                    cds.setStart(Integer.valueOf(tokens[3]));
                    cds.setEnd(Integer.valueOf(tokens[4]));
                    cds.setReverse(tokens[6].equals("-"));
                    if (!"+-".contains(tokens[6]))
                        System.err.println("Expected + or - in line: " + aLine);
                    proteinAccessionIterator.restart(tokens[8]);
                    if (proteinAccessionIterator.hasNext()) {
                        cds.setProteinId(proteinAccessionIterator.getFirst());
                        list.add(cds);
                    }  // else System.err.println("No protein id found for line: " + aLine);
                }
            }
        }
        return list;
    }

    /**
     * parse multi-line simple GFF format
     *
     * @param it
     * @param dnaAccession
     * @return annotations
     */
    private static Collection<CDS> parseSimpleGFFforCDS(Iterator<String> it, String dnaAccession) {
        final TaggedValueIterator proteinAccessionIterator = new TaggedValueIterator(false, true, "ref|");

        final ArrayList<CDS> list = new ArrayList<>(100000);
        if (it.hasNext()) {
            String aLine = it.next();
            if (aLine.length() > 0 && Character.isDigit(aLine.charAt(0))) { // coordinates
                while (it.hasNext()) { // only makes sense to process if there are more lines to be read, even if aLine!=null
                    final String[] tokens = Basic.split(aLine, '\t');
                    if (tokens.length == 3 && tokens[2].endsWith("CDS")) {
                        int a = Basic.parseInt(tokens[0]);
                        int b = Basic.parseInt(tokens[1]);
                        String proteinId = null;
                        while (it.hasNext()) {
                            aLine = it.next();
                            if (aLine.length() > 0 && Character.isDigit(aLine.charAt(0)))
                                break; // this is start of next feature
                            else if (aLine.contains("protein_id")) {
                                proteinAccessionIterator.restart(aLine);
                                if (proteinAccessionIterator.hasNext()) {
                                    proteinId = proteinAccessionIterator.next();
                                }

                            }
                        }
                        if (proteinId != null) {
                            CDS cds = new CDS();
                            cds.setDnaId(dnaAccession);
                            cds.setStart(Math.min(a, b));
                            cds.setEnd(Math.max(a, b));
                            cds.setReverse(a > b);
                            cds.setProteinId(proteinId);
                            list.add(cds);

                        }
                    } else
                        aLine = it.next();
                }
            }

        }
        return list;
    }
}
