package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(name = "img")
@XmlAccessorType(XmlAccessType.NONE)
public class Image {
    @XmlAttribute
    private String src;
    @XmlAttribute
    private String alt;
    @XmlAttribute
    private String title;
    @XmlAttribute
    private Integer width;
    @XmlAttribute
    private Integer height;
}
