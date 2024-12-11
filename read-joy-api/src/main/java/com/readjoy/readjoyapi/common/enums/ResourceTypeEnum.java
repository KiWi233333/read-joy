//package com.readjoy.readjoyapi.common.enums;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
///**
// * Resource type enum.
// */
//@Getter
//@AllArgsConstructor
//public enum ResourceTypeEnum {
//    // image/*, video/mp4, audio/mp3, application/zip, text/plain, application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.ms-powerpoint, application/vnd.openxmlformats-officedocument.presentationml.presentation, application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.oasis.opendocument.text, application/vnd.oasis.opendocument.spreadsheet, application/vnd.oasis.opendocument.presentation, application/epub+zip, application/x-mobipocket-ebook, application/x-tar, application/x-rar-compressed, application/x-7z-compressed, application/x-bzip2, application/x-gzip, application/x-xz, application/x-compress, application/x-bzip, application/x-lzip, application/x-lzma, application/x-lz
//    IMAGE(1, "image/*", "jpg,jpeg,png,gif,svg"),
//    VIDEO(2, "video/mp4", "mp4,mov,avi,wmv,flv,mkv,webm"),
//    AUDIO(3, "audio/mp3", "mp3,wav,flac,aac,m4a"),
//    ZIP(4, "application/zip", "zip,tar,gz,bz2,xz,rar,7z"),
//    TEXT(5, "text/plain", "txt,md,html,css,js,json,xml,csv,tsv"),
//    WORD(6, "application/msword", "doc,docx"),
//    PPT(7, "application/vnd.ms-powerpoint", "ppt,pptx"),
//    EXCEL(8, "application/vnd.ms-excel", "xls,xlsx"),
//    EPUB(9, "application/epub+zip", "epub"),
//    MOBI(10, "application/x-mobipocket-ebook", "mobi"),
//    ;
//
//    private final Integer code;
//    private final String value;
//    private final String extension;
//
//    public static ResourceTypeEnum fromCode(Integer code) {
//        for (ResourceTypeEnum resourceTypeEnum : ResourceTypeEnum.values()) {
//            if (resourceTypeEnum.getCode().equals(code)) {
//                return resourceTypeEnum;
//            }
//        }
//        return null;
//    }
//
//}
