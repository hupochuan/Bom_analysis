package indi.ycl.util;

public class CountChar {
	/**中文字符 */
    private int chCharacter = 0;
    
    /**英文字符 */
    private int enCharacter = 0;
    
    /**空格 */
    private int spaceCharacter = 0;
    
    /**数字 */
    private int numberCharacter = 0;
    
    /**其他字符 */
    private int otherCharacter = 0;
    
    /***
     * 统计字符串中中文，英文，数字，空格等字符个数
     * @param str 需要统计的字符串
     */
    public void count(String str) {
        if (null == str || str.equals("")) {
            System.out.println("字符串为空");
            return;
        }
        
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
            if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
                enCharacter ++;
            } else if ((tmp >= '0') && (tmp <= '9')) {
                numberCharacter ++;
            } else if (tmp ==' ') {
                spaceCharacter ++;
            } else if (isChinese(tmp)) {
                chCharacter ++;
            } else {
                otherCharacter ++;
            }
        }
//        System.out.println("字符串:" + str + "");
//        System.out.println("中文字符有:" + chCharacter);
//        System.out.println("英文字符有:" + enCharacter);
//        System.out.println("数字有:" + numberCharacter);
//        System.out.println("空格有:" + spaceCharacter);
//        System.out.println("其他字符有:" + otherCharacter);
    }
    
    /***
     * 判断字符是否为中文
     * @param ch 需要判断的字符
     * @return 中文返回true，非中文返回false
     */
    private boolean isChinese(char ch) {
        //获取此字符的UniCodeBlock
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        //  GENERAL_PUNCTUATION 判断中文的“号  
        //  CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号  
        //  HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号 
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS 
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B 
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS 
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            //System.out.println(ch + " 是中文");
            return true;
        }
        return false;
    }
    
    public static void main(String[] args) {
        String str = "                                                                             鲁西化工集团股份有限公司 2016 ";
//        CountChar countCharacter = new CountChar();
//        countCharacter.count(str);
        System.out.println(str.indexOf(" "));
        
        
    }

	public int getChCharacter() {
		return chCharacter;
	}

	public void setChCharacter(int chCharacter) {
		this.chCharacter = chCharacter;
	}

	public int getEnCharacter() {
		return enCharacter;
	}

	public void setEnCharacter(int enCharacter) {
		this.enCharacter = enCharacter;
	}

	public int getSpaceCharacter() {
		return spaceCharacter;
	}

	public void setSpaceCharacter(int spaceCharacter) {
		this.spaceCharacter = spaceCharacter;
	}

	public int getNumberCharacter() {
		return numberCharacter;
	}

	public void setNumberCharacter(int numberCharacter) {
		this.numberCharacter = numberCharacter;
	}

	public int getOtherCharacter() {
		return otherCharacter;
	}

	public void setOtherCharacter(int otherCharacter) {
		this.otherCharacter = otherCharacter;
	}

}
