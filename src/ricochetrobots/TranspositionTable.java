package ricochetrobots;

import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class TranspositionTable {
    private final long[] table;
    private final int mask;

    public TranspositionTable(int sizeBase) {
        int size = 1 << sizeBase;
        mask = size - 1;
        table = new long[size];
    }
    
    public int loadDepth(long hash) {
        long value = table[(int)hash & mask] ^ hash;
        if((value & ~0xffL) == 0) {
            return (int) value;
        }
        return -1;
    }
    
    public boolean load(long hash, TranspositionEntry entry) {
        entry.depth = loadDepth(hash);
        return entry.depth >= 0;
    }
    
    public void save(long hash, TranspositionEntry entry) {
        saveDepth(hash, entry.depth);
    }
    
    public void saveDepth(long hash, int depth) {
        table[(int)hash & mask] = hash ^ depth;
    }
    
    public void saveDepthIfEmpty(long hash, int depth) {
        int index = (int)hash & mask;
        if(table[index] == 0) {
            table[index] = hash ^ depth;
        }
    }
    
    public void saveDepthIfEmptyOrLess(long hash, int depth) {
        int index = (int)hash & mask;
        long value = table[index];
        if(value == 0 || (value ^ hash) < depth) {
            table[index] = hash ^ depth;
        }
    }
    
    public void clear() {
        Arrays.fill(table, 0);
    }
    
    public int count() {
        int count = 0;
        for (long entry : table) {
            if(entry != 0) {
                count++;
            }
        }
        return count;
    }

    public int size() {
        return table.length;
    }
}
