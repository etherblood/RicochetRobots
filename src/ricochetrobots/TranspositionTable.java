package ricochetrobots;

import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class TranspositionTable {
    private final static long EMPTY = 0;
    private final long[] table;
    private final int mask;

    public TranspositionTable(int sizeBase) {
        int size = 1 << sizeBase;
        mask = size - 1;
        table = new long[size];
    }
    
    public boolean load(long hash, TranspositionEntry entry) {
        long depth = table[(int)hash & mask] ^ hash;
        if((depth & ~mask) == 0) {
            entry.depth = (int) depth;
            return true;
        }
        return false;
    }
    
    public void save(long hash, TranspositionEntry entry) {
        saveDepth(hash, entry.depth);
    }
    
    public void saveDepth(long hash, int depth) {
        assert (mask & depth) == depth;
        table[(int)hash & mask] = hash ^ depth;
    }
    
    public void saveDepthIfEmpty(long hash, int depth) {
        assert (mask & depth) == depth;
        int index = (int)hash & mask;
        if(table[index] == EMPTY) {
            table[index] = hash ^ depth;
        }
    }
    
    public void clear() {
        Arrays.fill(table, EMPTY);
    }
    
    public int count() {
        int count = 0;
        for (long entry : table) {
            if(entry != EMPTY) {
                count++;
            }
        }
        return count;
    }

    public int size() {
        return table.length;
    }
}
