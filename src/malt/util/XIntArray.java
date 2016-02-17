/**
 * XIntArray.java 
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
package malt.util;

import megan.io.OutputWriter;

import java.io.IOException;
import java.util.Arrays;

/**
 * An extended array of integers
 * Daniel Huson 3/2015
 */
public class XIntArray {
    private final int SEGMENT_BITS;
    private final int SEGMENT_SIZE; // always a power of 2
    private final int SEGMENT_MASK;

    private int[][] segments;

    private long maxIndex = -1;

    /**
     * constructs a new array of initial size 0. If size is known, use other constructor
     *
     * @throws java.io.IOException
     */
    public XIntArray() throws IOException {
        this(0l);
    }

    /**
     * constructs a new array of the given size
     *
     * @throws java.io.IOException
     */
    public XIntArray(long size) {
        this((byte) (Math.min(30, 1 + Math.max(10, (int) (Math.log(size) / Math.log(2))))));
        int segment = (int) (size >>> SEGMENT_BITS);
        resize(segment + 1);
    }

    /**
     * constructs a new array using the given number of bits as segmentation key (in the range 10 and 30)
     *
     * @param bits
     * @throws java.io.IOException
     */
    public XIntArray(byte bits) {
        segments = new int[0][];
        SEGMENT_BITS = bits;
        SEGMENT_SIZE = (1 << (SEGMENT_BITS));
        SEGMENT_MASK = SEGMENT_SIZE - 1;

        System.err.println("SEGMENT_BITS: " + SEGMENT_BITS);
        System.err.println("SEGMENT_MASK: " + Integer.toBinaryString(SEGMENT_MASK));
        System.err.println("SEGMENT_SIZE: " + SEGMENT_SIZE);
    }

    /**
     * Zeros the array
     */
    public void clear() {
        for (int[] segment : segments) {
            Arrays.fill(segment, 0);
        }
        maxIndex = -1;
    }

    /**
     * put a value
     *
     * @param index
     * @param value
     */
    public void put(long index, int value) {
        segments[(int) (index >> SEGMENT_BITS)][(int) (index & SEGMENT_MASK)] = value;
        maxIndex = Math.max(maxIndex, index);
    }

    /**
     * put a value, If the index is larger than current maxIndex(), increases length of array
     *
     * @param index
     * @param value
     */
    public void putAndEnsureCapacity(long index, int value) {
        int segment = (int) (index >>> SEGMENT_BITS);
        int position = (int) (index & SEGMENT_MASK);
        if (segment >= segments.length)
            resize(segment + 1);
        segments[segment][position] = value;
        maxIndex = Math.max(maxIndex, index);
    }

    /**
     * get a value, no checks, assumes that all entries have been set
     *
     * @param index
     * @return value
     */
    public int get(long index) {
        return segments[(int) (index >>> SEGMENT_BITS)][(int) (index & SEGMENT_MASK)];
    }

    /**
     * resizes the array
     */
    private void resize(int newLength) {
        final int[][] tmp = new int[newLength][];
        System.arraycopy(segments, 0, tmp, 0, segments.length);
        for (int i = segments.length; i < newLength; i++) {
            tmp[i] = new int[SEGMENT_SIZE];
        }
        segments = tmp;
    }

    /**
     * returns the first n values
     *
     * @param n
     * @return first n values as string
     */

    public String toString(long n) {
        StringBuilder buf = new StringBuilder();
        for (long i = 0; i < Math.min(n, length()); i++) {
            buf.append(" ").append(get(i));
        }
        return buf.toString();
    }

    public long getMaxIndex() {
        return maxIndex;
    }

    public long length() {
        return maxIndex + 1;
    }

    /**
     * write to stream in binary
     *
     * @param outs
     */
    public void write(OutputWriter outs) throws IOException {
        for (long index = 0; index < maxIndex; index++) {
            outs.writeInt(get(index));
        }
    }

    public static void main(String[] args) {
        XIntArray array = new XIntArray((byte) 30);

        for (int i = 0; i < 1000000000; i++) {
            array.putAndEnsureCapacity(i, i);
        }

        for (int i = 0; i < 1000000000; i++) {
            if (array.get(i) != i)
                System.err.println("Error: " + i + " != " + array.get(i));
        }

        System.err.println("Segments: " + array.segments.length);
    }
}
