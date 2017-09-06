package com.github.nginate.wolframalpha.comparator;

import com.github.nginate.wolframalpha.model.*;
import com.sun.org.apache.xerces.internal.dom.ElementImpl;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

@Slf4j
@UtilityClass
public class TestComparators {

    public static <T extends Comparable<T>> Comparator<Collection<T>> collectionComparator() {
        return collectionComparator(Comparable::compareTo);
    }

    public static <T> Comparator<Collection<T>> collectionComparator(Comparator<T> comparator) {
        return (o1, o2) -> {
            if (o1 == o2) {
                return 0;
            }

            if (o1 == null) {
                log.debug("First object is null");
                return 1;
            }

            if (o2 == null) {
                log.debug("Second object is null");
                return -1;
            }

            Set<T> o1Unique = new HashSet<>(o1);
            Set<T> o2Unique = new HashSet<>(o2);
            if (o1Unique.size() != o2Unique.size()) {
                return Comparator.<Set<T>>comparingInt(Set::size).compare(o1Unique, o2Unique);
            }

            if (o1Unique.containsAll(o2Unique)) {
                return 0;
            }

            Iterator<T> o1Iterator = o1.iterator();
            Iterator<T> o2Iterator = o2.iterator();

            while (o1Iterator.hasNext() && o2Iterator.hasNext()) {
                T o1Current = o1Iterator.next();
                T o2Current = o2Iterator.next();

                //noinspection unchecked
                try {
                    int result = comparator.compare(o1Current, o2Current);
                    if (result != 0) {
                        log.debug("Non equal objects : {}, {}", o1Current, o2Current);
                        return result;
                    }
                } catch (NullPointerException e) {
                    log.error("Nested comparator failed", e);
                    throw e;
                }
            }

            return 0;
        };
    }

    public static Comparator<Pod> podComparator() {
        return Comparator.comparing(Pod::getTitle)
                .thenComparing(Pod::getError)
                .thenComparing(Pod::getPosition)
                .thenComparing(Pod::getScanner)
                .thenComparing(Pod::getId)
                .thenComparing(Pod::getNumsubpods)
                .thenComparing(Pod::getPrimary)
                .thenComparing(Pod::getSubpods, collectionComparator(subpodComparator()))
                .thenComparing(Pod::getSounds, nullsFirst(soundsComparator()));
    }

    public static Comparator<Subpod> subpodComparator() {
        return Comparator.comparing(Subpod::getTitle)
                .thenComparing(Subpod::getPrimary)
                .thenComparing(Subpod::getMinput, nullsFirst(naturalOrder()))
                .thenComparing(Subpod::getMoutput, nullsFirst(naturalOrder()))
                .thenComparing(Subpod::getPlaintext, nullsFirst(naturalOrder()))
                .thenComparing(Subpod::getMathMl, nullsFirst(mathMlComparator()))
                .thenComparing(Subpod::getImg, nullsFirst(imageComparator()))
                .thenComparing(Subpod::getImageMap, nullsFirst(imageMapComparator()));
    }

    public static Comparator<Sounds> soundsComparator() {
        return Comparator.comparing(Sounds::getCount)
                .thenComparing(Sounds::getSounds, collectionComparator(soundComparator()));
    }

    public static Comparator<Sounds.Sound> soundComparator() {
        return Comparator.comparing(Sounds.Sound::getType)
                .thenComparing(Sounds.Sound::getUrl);
    }

    public static Comparator<ElementImpl> mathMlComparator() {
        return Comparator.comparing(ElementImpl::toString);
    }

    public static Comparator<ImageMap> imageMapComparator() {
        return Comparator.comparing(ImageMap::getRectangles, collectionComparator(imageRectangleComparator()));
    }

    public static Comparator<ImageRectangle> imageRectangleComparator() {
        return Comparator.comparing(ImageRectangle::getTitle)
                .thenComparing(ImageRectangle::getBottom)
                .thenComparing(ImageRectangle::getTop)
                .thenComparing(ImageRectangle::getLeft)
                .thenComparing(ImageRectangle::getBottom)
                .thenComparing(ImageRectangle::getQuery)
                .thenComparing(ImageRectangle::getAssumptions);
    }

    public static Comparator<Image> imageComparator() {
        return Comparator.comparing(Image::getTitle)
                .thenComparing(Image::getAlt)
                .thenComparing(Image::getHeight)
                .thenComparing(Image::getWidth);
    }

    public static Comparator<Assumptions> assumptionsComparator() {
        return Comparator.comparing(Assumptions::getCount)
                .thenComparing(Assumptions::getAssumptions, collectionComparator(assumptionComparator()));
    }

    public static Comparator<Assumptions.Assumption> assumptionComparator() {
        return Comparator.comparing(Assumptions.Assumption::getType)
                .thenComparing(Assumptions.Assumption::getWord)
                .thenComparing(Assumptions.Assumption::getTemplate)
                .thenComparing(Assumptions.Assumption::getCount)
                .thenComparing(Assumptions.Assumption::getValues, collectionComparator(assumptionValueComparator()));
    }

    public static Comparator<Assumptions.Assumption.AssumptionValue> assumptionValueComparator() {
        return Comparator.comparing(Assumptions.Assumption.AssumptionValue::getDescription)
                .thenComparing(Assumptions.Assumption.AssumptionValue::getInput)
                .thenComparing(Assumptions.Assumption.AssumptionValue::getName);
    }
}
