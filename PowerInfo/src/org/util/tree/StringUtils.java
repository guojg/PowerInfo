package org.util.tree;

public class StringUtils {
    
    /**
     * ����һ�������Stringֵ
     * ���Ϊ�գ����ؿ��ַ���Ϊ�գ������ظö�����ַ��ʽ
     * @param o 
     * @return
     */
    public static String nvl(Object o) {
        return o == null ? "" : o.toString();
    }

}
