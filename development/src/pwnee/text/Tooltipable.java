package pwnee.text;

/** Any kind of component that can have a tooltip displayed for it. */
public interface Tooltipable {
  
  /** Returns the string to display as the tooltip for the component. */
  public String getTooltipString();
  
  /** The x position of the component, in the geometric space that it and its tooltip are rendered in. */
  public double getX();
  
  /** The y position of the component, in the geometric space that it and its tooltip are rendered in. */
  public double getY();
}