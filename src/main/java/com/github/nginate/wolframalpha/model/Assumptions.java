package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * The <assumptions> element is a subelement of <queryresult>. Its content is a series of <assumption> elements. It has
 * a count attribute, giving the number of <assumption> subelements.
 * <p>
 * To apply an assumption in a query, use the assumption parameter. The value you pass for this parameter is the string
 * found in the input attribute of a <value> element returned from a previous query. Here is how to invoke the query
 * "pi" but specify that you want pi treated as the name of a movie. The obscure-looking assumption string here was
 * taken from the earlier <assumptions> output for this query.
 * <pre>
 *     http://api.wolframalpha.com/v2/query?input=pi&appid=XXXX&assumption=*C.pi-_*Movie-Here
 * </pre>
 */
@Data
@XmlType(name = "assumptions")
@XmlAccessorType(XmlAccessType.NONE)
public class Assumptions {
    @XmlAttribute
    private Integer count;
    @XmlElement(name = "assumption")
    private List<Assumption> assumptions;

    /**
     * The <assumption> element is a subelement of <assumptions>. It defines a single assumption, typically about the
     * meaning of a word or phrase, and a series of possible other values. It has the following attributes:
     */
    @Data
    @XmlType(name = "assumption")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Assumption {
        /**
         * Classification of an assumption that defines how it will function.
         */
        @XmlAttribute
        private AssumptionType type;
        /**
         * The central word/phrase to which the assumption is applied.
         */
        @XmlAttribute
        private String word;
        /**
         * A statement outlining the way an assumption will be applied.
         */
        @XmlAttribute
        private String template;
        /**
         * Number of possible values available from an assumption.
         */
        @XmlAttribute
        private Integer count;
        /**
         * List of assumption values
         *
         * @see Assumption
         */
        @XmlElement(name = "value")
        private List<AssumptionValue> values;

        /**
         * Classification of an assumption that defines how it will function.
         */
        @XmlEnum
        public enum AssumptionType {
            /**
             * The Clash assumption is generated when a word can represent different categories of things, such as "pi"
             * being treated as a mathematical constant, a movie, a character, or simply as a word.
             */
            @XmlEnumValue("Clash")
            CLASH,
            /**
             * The Unit assumption is generated when a word is interpreted as a unit abbreviation, but it is ambiguous
             * as to what unit it represents. An example is "m", meaning either meters or minutes
             */
            @XmlEnumValue("Unit")
            UNIT,
            /**
             * The AngleUnit assumption is generated when a number is interpreted as a unit of angle, but it is
             * ambiguous whether it should be interpreted as degrees or radians. This assumption type always has two
             * <value> elements, one for the assumption of degrees and the other for the assumption of radians.
             */
            @XmlEnumValue("AngleUnit")
            ANGLE_UNIT,
            /**
             * The Function assumption is generated when a word is interpreted as referring to a mathematical function,
             * but it is ambiguous which function is meant. An example is "log" meaning either log base e or log base
             * 10.
             */
            @XmlEnumValue("Function")
            FUNCTION,
            /**
             * The MultiClash assumption is a type of clash where multiple overlapping strings can have different
             * interpretations. An example is the query "log 0.5", where the whole phrase can be interpreted as the
             * mathematical object "log(0.5)", or the word "log" can be interpreted as a probability distribution or a
             * plotting function
             */
            @XmlEnumValue("MultiClash")
            MULTI_CLASH,
            /**
             * The SubCategory assumption is similar to the Clash type in that a word can refer to multiple types of
             * entities, but for SubCategory all the interpretations are within the same overall category. An example is
             * the query "hamburger", which generates a SubCategory assumption for different types of hamburger (basic
             * hamburger, McDonald's hamburger, Burger King hamburger, etc.) The hamburger query also generates a Clash
             * assumption over whether hamburger should be treated as a type of food or a simple word, but given that
             * Wolfram|Alpha is treating hamburger as a type of food in this query, it also can be resolved into
             * subcategories of hamburger.
             */
            @XmlEnumValue("SubCategory")
            SUB_CATEGORY,
            /**
             * You can think of the Attribute assumption as the next step down in the sequence of Clash and SubCategory.
             * Wolfram|Alpha emits an Attribute assumption to allow you to modify an attribute of an already
             * well-characterized entity. In the query "hamburger", Wolfram|Alpha assumes you mean that hamburger is a
             * food item (although it gives you a Clash assumption to modify this) and that you mean a "basic" hamburger
             * (and it gives you a SubCategory assumption to make this, say, a McDonald's hamburger). It also gives you
             * an Attribute assumption to modify details like patty size and whether it has condiments. The name
             * attributes for Attribute assumptions can become rather cryptic, but the desc attributes are much clearer
             * to understand
             */
            @XmlEnumValue("Attribute")
            ATTRIBUTE,
            /**
             * When Wolfram|Alpha recognizes a string in a query as referring to a time, and it is ambiguous as to
             * whether it represents AM or PM, a TimeAMOrPM assumption is generated. There are always two <value>
             * elements in this assumption: one for AM and one for PM. As always, the firstlisted one is the current
             * value for the assumption, and this will depend on what time of day the query is executed.
             */
            @XmlEnumValue("TimeAMOrPM")
            TIME_AM_OR_PM,
            /**
             * When Wolfram|Alpha recognizes a string in a query as referring to a date in numerical format, and it is
             * ambiguous as to the order of the day, month, and year elements (such as 12/11/1996), a DateOrder
             * assumption is generated. The number and order of <value> elements depends on specifics of the date string
             * in the query, and also on the locale of the caller. The name attributes will be a combination of Day,
             * Month, and Year in the corresponding order.
             */
            @XmlEnumValue("DateOrder")
            DATE_ORDER,
            /**
             * The ListOrTimes assumption is generated when a query contains elements separated by spaces and it is
             * unclear whether this is to be interpreted as multiplication or a list of values. For example, the query
             * "3 x" is interpreted as 3*x, but it could also be the list {3, x}. The ListOrTimes assumption always has
             * two <value> elements: one named "List" and one named "Times." There is no word attribute in the
             * <assumption> element for this type.
             */
            @XmlEnumValue("ListOrTimes")
            LIST_OR_TIMES,
            /**
             * The ListOrNumber assumption is generated when a query contains a string that could be either a number
             * with a comma as a thousands separator, or a list of two separate numbers, such as the query "1,234.5.".
             * The ListOrNumber assumption always has two <value> elements: one named "List" and one named "Number."
             */
            @XmlEnumValue("ListOrNumber")
            LIST_OR_NUMBER,
            /**
             * The CoordinateSystem assumption is generated when it is ambiguous which coordinate system a query refers
             * to. For example, the query "div(x rho,y z,z x)" mixes elements from standard notation for 3D Cartesian
             * coordinates and cylindrical coordinates. The possible values for the name attribute are Cartesian2D,
             * Cartesian3D, Polar2D, Cylindrical3D, Spherical3D, General2D, and General3D. There is no word attribute in
             * the <assumption> element for this type.
             */
            @XmlEnumValue("CoordinateSystem")
            COORDINATE_SYSTEM,
            /**
             * The I assumption is generated when a query uses "i" in a way that could refer to a simple variable name
             * (similar to, say, "x") or the mathematical constant equal to the square root of -1. The I assumption
             * always has two <value> elements: one named "ImaginaryI" and one named "Variable". There is no word
             * attribute in the <assumption> element for this type, as it always refers to the letter "i".
             */
            @XmlEnumValue("I")
            I,
            /**
             * The NumberBase assumption is generated when a number could be interpreted as being written in more than
             * one base, such as "100110101", which looks like a binary number (base 2) but could also be base 10 (it
             * could be other bases as well, but those are rarely used and thus do not occur as assumption values). At
             * the present time, the only possible <value> elements for this assumption are "Decimal" and "Binary".
             */
            @XmlEnumValue("NumberBase")
            NUMBER_BASE,
            /**
             * The MixedFraction assumption is generated when a string could be interpreted as either a mixed fraction
             * or a multiplication, such as "3 1/2". The MixedFraction assumption always has two <value> elements: one
             * named "Mix" and one named "Mult."
             */
            @XmlEnumValue("MixedFraction")
            MIXED_FRACTION,
            /**
             * The MortalityYearDOB assumption is a very specialized type generated in some mortality-related queries,
             * such as "life expectancy France 1910". The year 1910 could refer to the year of the data (that is, life
             * expectancy data from France in the year 1910), or the year of birth ("life expectancy data in France for
             * people born in 1910"). The MortalityYearDOB assumption distinguishes between those two meanings. The
             * MortalityYearDOB assumption always has two <value> elements: one named "Year" and one named
             * "DateOfBirth."
             */
            @XmlEnumValue("MortalityYearDOB")
            MORTALITY_YEAR_DOB,
            /**
             * The TideStation assumption is generated in tide-related queries. It distinguishes between different tide
             * stations. Here is an example from the query "tides Seattle", which is the query that was used to generate
             * Figure 1 at the beginning of this document:
             * <pre>
             *  <assumption type='TideStation' count='5'>
             *      <value name='PrimaryStation'
             *          desc='nearest primary station'
             *          input='TideStation_PrimaryStation' />
             *      <value name='NearestStation'
             *          desc='nearest station'
             *          input='TideStation_NearestStation' />
             *      <value name='Seattle, Washington (1.7 mi)'
             *          desc='Seattle, Washington (1.7 mi)'
             *          input='TideStation_*UnitedStates .9447130.PrimaryStation-' />
             *      <value name='Bangor, Washington (20.2 mi)'
             *          desc='Bangor, Washington (20.2 mi)'
             *          input='TideStation_*UnitedStates .9445133.PrimaryStation-' />
             *      <value name='Tacoma, Washington (25.2 mi)'
             *          desc='Tacoma, Washington (25.2 mi)'
             *          input='TideStation_*UnitedStates .9446484.PrimaryStation-' />
             *  </assumption>
             * </pre>
             */
            @XmlEnumValue("TideStation")
            TIDE_STATION,
            /**
             * Some queries have more than one formula that applies. The FormulaSelect assumption allows you to choose
             * the one you want. In 'Doppler shift' example, you can choose the classical Doppler shift formula (the
             * default) or the relativistic one.
             */
            @XmlEnumValue("FormulaSelect")
            FORMULA_SELECT,
            /**
             * Formulas can be rearranged to solve for different variables. The FormulaSelect assumption lets you pick
             * which one you want. In 'Doppler shift' example, the variables are the frequency reduction factor (fo/fs ,
             * treated as a single entity), the speed of sound (c), and the speed of the source (vs ). Notice in the
             * Result pod it shows a value for frequency reduction factor, which is the current choice for the variable
             * to solve for. If you were to choose a different variable to solve for, it would show that value in this
             * pod.
             */
            @XmlEnumValue("FormulaSolve")
            FORMULA_SOLVE,
            /**
             * The FormulaVariable assumption lets you supply a value for a variable in a formula. It corresponds to an
             * input field or pulldown menu of choices on the website.
             */
            @XmlEnumValue("FormulaVariable")
            FORMULA_VARIABLE,
            /**
             * Wolfram|Alpha can sometimes present the same basic formula in terms of a different set of variables. In
             * the 'Doppler shift' example, you can choose to have the frequency reduction factor (fo/fs ) broken up
             * into two separate variables (fo and fs ). You're not substituting a completely different formula (like
             * FormulaSelect) or simply adding a new variable (like FormulaVariableInclude).
             */
            @XmlEnumValue("FormulaVariableOption")
            FORMULA_VARIABLE_OPTION,
            /**
             * The FormulaVariableInclude assumption lets you add additional variables into a formula. For simplicity,
             * Wolfram|Alpha presents the Doppler shift formula with a small number of variables, but it knows how to
             * include two more: the speed of the observer and the wind speed. On the website, if you click to add one
             * of these variables, the formula will change to include this variable, the tabular results will get an
             * extra row for it, and you will get an extra input field to enter its value.
             */
            @XmlEnumValue("FormulaVariableInclude")
            FORMULA_VARIABLE_INCLUDE
        }

        /**
         * All assumption types have the same basic structure: a sequence of <value> subelements, one for each possible
         * value of the assumption. Each <value> element has three attributes: name, which is a unique internal
         * identifier, but which will often have some descriptive value to the programmer; desc, which is a textual
         * description suitable for displaying to users; and input, which gives the parameter value needed to invoke
         * this assumption in a subsequent query. The first-listed <value> element always names the assumption value
         * that was in effect for the current query
         */
        @Data
        @XmlType(name = "value")
        @XmlAccessorType(XmlAccessType.NONE)
        public static class AssumptionValue {
            /**
             * a unique internal identifier, but which will often have some descriptive value to the programmer
             */
            @XmlAttribute
            private String name;
            /**
             * a textual description suitable for displaying to users
             */
            @XmlAttribute(name = "desc")
            private String description;
            /**
             * gives the parameter value needed to invoke this assumption in a subsequent query
             */
            @XmlAttribute
            private String input;
        }
    }
}
