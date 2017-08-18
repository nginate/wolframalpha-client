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
    @XmlElement
    private String plaintext;
}
