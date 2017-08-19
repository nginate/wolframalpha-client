package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlType(name = "subpod")
@XmlAccessorType(XmlAccessType.NONE)
public class Subpod {
    /**
     * usually an empty string because most subpods have no title
     */
    @XmlAttribute
    private String title;
    @XmlAttribute
    private Boolean primary;

    @XmlElement
    private Image img;
    @XmlElement(name = "imagemap")
    private ImageMap imageMap;
    @XmlElement
    private String plaintext;
    @XmlElement(name = "mathml")
    private Object mathMl;
}
