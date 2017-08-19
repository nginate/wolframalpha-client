package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlType(name = "imagemap")
@XmlAccessorType(XmlAccessType.NONE)
public class ImageMap {
    @XmlElement(name = "rect")
    private List<ImageRectangle> rectangles;
}
