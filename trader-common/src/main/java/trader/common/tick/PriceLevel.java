package trader.common.tick;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import trader.common.util.ConversionUtil;
import trader.common.util.StringUtil;

public class PriceLevel {
    private static final Pattern PATTERN = Pattern.compile("([a-z]+)(\\d*)");
    private static final Map<String, PriceLevel> levels = new HashMap<>();

    public static final String LEVEL_MIN  = "min";
    public static final String LEVEL_VOL  = "vol";

    public static final PriceLevel TICKET = new PriceLevel("tick", -1);
    public static final PriceLevel MIN1 = PriceLevel.valueOf(LEVEL_MIN+1);
    public static final PriceLevel MIN3 = PriceLevel.valueOf(LEVEL_MIN+3);
    public static final PriceLevel MIN5 = PriceLevel.valueOf(LEVEL_MIN+5);
    public static final PriceLevel MIN15 = PriceLevel.valueOf(LEVEL_MIN+15);
    public static final PriceLevel HOUR = PriceLevel.valueOf(LEVEL_MIN+60);

    public static final PriceLevel VOL1K = PriceLevel.valueOf(LEVEL_VOL+"1k");
    public static final PriceLevel VOL3K = PriceLevel.valueOf(LEVEL_VOL+"3k");
    public static final PriceLevel VOL5K = PriceLevel.valueOf(LEVEL_VOL+"5k");
    public static final PriceLevel VOL10K = PriceLevel.valueOf(LEVEL_VOL+"10k");

    public static final PriceLevel DAY = new PriceLevel("day", -1);

    private String name;
    private int value;

    private PriceLevel(String name, int levelValue){
        this.name = name;
        this.value = levelValue;
    }

    public String name() {
        return name;
    }

    public int getValue(){
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if ( o==null || !(o instanceof PriceLevel)) {
            return false;
        }
        PriceLevel l = (PriceLevel)o;

        return name.equals(l.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static PriceLevel valueOf(String str){
        str = str.trim().toLowerCase();
    	PriceLevel result = levels.get(str);
    	if ( result==null ) {
    	    String levelName = str;
            int unit=1;
    	    if ( str.endsWith("k") ) {
                unit = 1000;
                str = str.substring(0, str.length()-1);
            }
    	    Matcher matcher = PATTERN.matcher(str);
    	    if ( matcher.matches() ) {
    	        String prefix = matcher.group(1);
    	        int value = -1;
    	        try {
    	            String vol = matcher.group(2).toLowerCase();
    	            if ( !StringUtil.isEmpty(vol)) {
    	                value = ConversionUtil.toInt(vol)*unit;
    	            }
    	        }catch(Throwable t) {}
    	        result = new PriceLevel(levelName, value);
    	        levels.put(str, result);
    	    }
    	}
    	return result;
    }

    public static PriceLevel minute2level(int minute){
        switch(minute){
        case 1:
            return PriceLevel.MIN1;
        case 3:
            return PriceLevel.MIN3;
        case 5:
            return PriceLevel.MIN5;
        case 15:
            return PriceLevel.MIN15;
        case 60:
            return PriceLevel.HOUR;
        default:
            throw new RuntimeException("Unsupported minute level: "+minute);
        }
    }

}
