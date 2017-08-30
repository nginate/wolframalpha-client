package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(name = "cell")
@XmlAccessorType(XmlAccessType.NONE)
public class Cell {
    @XmlAttribute
    private Boolean compressed;
}
