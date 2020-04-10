package util;
/*Editor: Johnson Gao 
 * Date This File Created: 2020-1-26 17:57:40
 * Description Of This Class: This is for processing css declearation.
This is a class which allows me to use the style attribute/element without be given a zero.
This class can also break down a css declearation and allows me to edit each indivisual roll without manually fixing the string.
Original idea from Johnson Gao, Created by Johnson Gao.
 */
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Johnson Gao
 */
public class SmartCss
{

    //declear global variable
    private String target;
    //private String state;
    //css rules
    private ArrayList<String[]> rules;

    /**
     * Construct a new CSS declearation object with given declearation text.
     *
     * @param cssDeclearText The declearation text for CSS.
     */
    public SmartCss(String cssDeclearText)
    {

        //DECLEAR need to user variables
        String ruleText;
        String[] ruleTexts;
        if (cssDeclearText.contains("{"))
        {
            //in case received a big declearation. (formal CSS)
            target = cssDeclearText.substring(0, cssDeclearText.indexOf('{'));
            ruleText = cssDeclearText.substring(cssDeclearText.indexOf("{") + 1, cssDeclearText.lastIndexOf("}"));
            //System.out.println(cssDeclearText.indexOf("{"));
            //System.out.println("target=" + target + "\n rule text = " + ruleText);
            ruleTexts = ruleText.split(";");
            rules = new ArrayList<>();
            for (String ruleText1 : ruleTexts)
            {
                rules.add(ruleText1.split(":"));
                //System.out.println(Arrays.toString(ruleText1.split(":")));
            }
        } else
        {
            //received a style attribute. Adding the rules.
            ruleTexts = cssDeclearText.replace("style=", "").replace("\"", "").split(";");
            for (String ruleText1 : ruleTexts)
            {
                rules.add(ruleText1.split(":"));
            }
            target = "";
        }
//        System.out.println();
    }

    /**
     * Construct a default CSS declearation object with nothing in it.
     */
    public SmartCss()
    {
        target = "";
        rules = new ArrayList<>();
    }

    /**
     * Get the target of this declearation.
     *
     * @return The tag of the css declearation.
     */
    public String getTarget()
    {
        return target;
    }

    /**
     * Set the target of this declearation.
     *
     * @param target The tag of the css declearation.
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    /**
     * Get the rules of the CSS.
     *
     * @return CSS rule.
     */
    public ArrayList<String[]> getRules()
    {
        return rules;
    }

//    public void setRules(ArrayList<String[]> rules)
//    {
//        this.rules = rules;
//    }
    /**
     * Check if certain CSS property is defined.
     *
     * @param name The name of the property to be checked.
     * @return <code>true</code> if the property is defined.
     * <code>false</code>otherwise.
     */
    public boolean isPropertyDefined(String name)
    {
        return getPropertySequenceIndex(name) != -1;
    }

    /**
     * Get the sequence number of the CSS decleared in this object. Where is the
     * css property.
     *
     * @param name The CSS property to search.
     * @return Either the sequence number found or <code>-1</code> means not
     * found.
     */
    public int getPropertySequenceIndex(String name)
    {
        //declear variable
        String[] get;
        //compare each of them with the given name.
        for (int i = 0; i < rules.size(); i++)
        {
            get = rules.get(i);
            if (get[0].equals(name))
            {
                //if the name fits, return and end the loop.
                return i;
            }
        }
        //else. return with -1 shows there is no such thing decleared.
        return -1;
    }

    /**
     * Set the property of css. Declar one property.
     *
     * @param name The name of the property.
     * @param value The value for the property.
     */
    public void setProperty(String name, String value)
    {
        int index = getPropertySequenceIndex(name);
        if (index == -1)
        {
            rules.add(new String[]
            {
                name, value
            });
        } else
        {
            rules.set(index, new String[]
            {
                name, value
            });
        }

    }

    /**
     * Get the CSS declaration for the HTML style attribute.
     *
     * @return The CSS declearation within the "style" attribute (for a tag).
     */
    public String getAttributeStyle()
    {
        String style = "style=\"";
        if (rules.isEmpty())
        {
            return "";
        } else
        {
            for (int i = 0; i < rules.size(); i++)
            {
                String[] get = rules.get(i);
                style += get[0] + ":" + get[1] + ";";
            }
            return style + "\"";
        }

    }

    /**
     * Get the CSS declearation this object is having.
     *
     * @return CSS declearation.
     */
    public String getDeclearation()
    {
        if (target.isEmpty())
        {
            //Error: the CSS real declearation must have a target.
            throw new IllegalArgumentException("There is no target given.");
        } else
        {
            //make the declearation
            String style = target + " \n{";
            for (int i = 0; i < rules.size(); i++)
            {
                String[] get = rules.get(i);
                style += "\n" + get[0] + ":" + get[1] + ";";
            }
            //append the end
            style += "}";
            return style;
        }
    }

    /**
     * Swap the sequence of two CSS declearations.
     *
     * @param n1 The first member to swap.
     * @param n2 The second member to swap.
     */
    public void swapMembersSequence(int n1, int n2)
    {
        Collections.swap(rules, n1, n2);
    }

    /**
     * Swap the sequence of two CSS declearations.
     *
     * @param n1 The first member to swap.
     * @param n2 The second member to swap.
     */
    public void swapMembersSequence(String n1, String n2)
    {
        swapMembersSequence(getPropertySequenceIndex(n1), getPropertySequenceIndex(n2));
    }

    /**
     * Get the css declearation to use inside HTML using "style" element.
     *
     * @return The CSS declearation with HTML style element.
     */
    public String getDeclearationHTML()
    {
        return "<style>\n" + getDeclearation() + "\n</style>";
    }

    /**
     * Test main method.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        SmartCss smartCss = new SmartCss("body{background-color:white;color:yellow;margin-top:150px;}");
        System.out.println(smartCss.getPropertySequenceIndex("color"));
        System.out.println(smartCss.getAttributeStyle());
        System.out.println(smartCss.getDeclearation());
        smartCss.setProperty("margin", "auto");
        smartCss.setProperty("color", "white");
        smartCss.setProperty("padding", "4px");
        System.out.println(smartCss);

    }

    @Override
    public String toString()
    {
        return getDeclearation();
    }
}
