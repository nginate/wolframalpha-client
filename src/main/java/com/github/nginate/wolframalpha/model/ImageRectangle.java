package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * subelements that specify the corners of clickable areas with coordinates (using the top-left corner as the origin)
 */
@Data
@XmlType(name = "rect")
@XmlAccessorType(XmlAccessType.NONE)
public class ImageRectangle {
    @XmlAttribute
    private Integer left;
    @XmlAttribute
    private Integer right;
    @XmlAttribute
    private Integer top;
    @XmlAttribute
    private Integer bottom;
    @XmlAttribute
    private String query;
    @XmlAttribute
    private String assumptions;
    @XmlAttribute
    private String title;
}
