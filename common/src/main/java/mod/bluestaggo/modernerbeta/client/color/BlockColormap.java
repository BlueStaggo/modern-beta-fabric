package mod.bluestaggo.modernerbeta.client.color;

public class BlockColormap {
    private final int[] colormap;
    
    public BlockColormap() {
        this.colormap = new int[65536];
    }
    
    public void setColormap(int[] map) {
        if (map.length != 65536)
            throw new IllegalArgumentException("[Modern Beta] Color map is an invalid size!");

        System.arraycopy(map, 0, this.colormap, 0, colormap.length);
    }
    
    public int getColor(double temp, double rain) {
        int rainNdx = (int)((1.0 - (rain * temp)) * 255.0);
        int tempNdx = (int)((1.0 - temp) * 255.0);
        int ndx = rainNdx << 8 | tempNdx;
        
        if (ndx >= this.colormap.length) {
            return 0xFFFF00FF;
        }
        
        return this.colormap[ndx];
    }
}
