package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlType(name = "sounds")
@XmlAccessorType(XmlAccessType.NONE)
public class Sounds {
    @XmlAttribute
    private Integer count;
    @XmlElement(name = "sound")
    private List<Sound> sounds;

    /**
     * HTML <sound> elements suitable for direct inclusion in a webpage. They point to stored sound files giving an audio representation of a single subpod. These elements only appear in pods if the requested result formats include sound or wav. The type attribute will tell whether the format is MIDI or WAV.
     */
    @Data
    @XmlType(name = "sound")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Sound {
        @XmlAttribute
        private String url;
        @XmlAttribute
        private AudioType type;

        @XmlType(name = "type")
        @XmlEnum
        public enum AudioType {
            @XmlEnumValue("audio/wav")
            WAV,
            @XmlEnumValue("audio/midi")
            MIDI
        }
    }
}
