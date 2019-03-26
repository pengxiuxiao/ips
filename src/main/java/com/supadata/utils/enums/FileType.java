package com.supadata.utils.enums;
/**
 * 功能描述:分类文件类型
 * @auther: pxx
 * @param:
 * @return:
 * @date: 2018/6/20 18:30
 */
public enum FileType {


    TYPE1("word", ""),
    TYPE2("image", ".png"),TYPE3("image", ".jpg"),
    TYPE4("image", ".PNG"),TYPE5("image", ".JPG"),
    TYPE12("image", ".JPEG"),TYPE13("image", ".jpeg"),
    TYPE6("video", ".MP4"),TYPE7("video", ".mp4"),
    TYPE14("video", ".AVI"),TYPE15("video", ".avi"),
    TYPE16("video", ".MOV"),TYPE17("video", ".mov"),
    TYPE8("ppt", ".ppt"),TYPE9("ppt", ".pptx"),
    TYPE10("ppt", ".PPT"),TYPE11("ppt", ".PPTX");
    // 成员变量
    private String name;
    private String value;
    // 构造方法
    private FileType(String value, String name) {
        this.name = name;
        this.value = value;
    }
    // 普通方法
    public static String getKey(String name) {
        for (FileType c : FileType.values()) {
            if (c.getName().equals(name)) {
                return c.value;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
