package king.lib.script.view;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jsyntaxpane.DefaultSyntaxKit;

import king.lib.script.model.Language;
import king.lib.script.model.Script;

/**
 * A syntax editor panel.
 * <p>
 * Required JAR files:
 * <ul>
 *   <li>jsyntaxpane-0.9.5-jre5-minimal.jar
 * </ul>
 * 
 * @author noblemaster
 * @since November 2, 2009
 */
public class ScriptSyntaxPanel extends JPanel {

  /** The ID. */
  private long id;
  /** The language. */
  private Language language;
  
  /** The text. */
  private JEditorPane codeArea;
  
  
  /**
   * The constructor.
   */
  public ScriptSyntaxPanel() {
    // set the look
    setOpaque(true);
    setLayout(new BorderLayout());
 
    // add the code area
    codeArea = new JEditorPane();
    add(new JScrollPane(codeArea), BorderLayout.CENTER);
  }
  
  /**
   * Returns the script.
   * 
   * @return  The script.
   */
  public Script getScript() {
    Script script = new Script();
    script.setId(id);
    script.setLang(language.getName());
    script.setCode(codeArea.getText());
    return script;
  }
  
  /**
   * Sets a script.
   * 
   * @param script  The script.
   */
  public void setScript(Script script) {
    // set the syntax panel settings
    id = script.getId();
    language = Language.find(script.getLang());
    DefaultSyntaxKit.initKit();
    if (language == Language.JAVA) {
      codeArea.setContentType("text/java");
    }
    else if (language == Language.PNUTS) {
      codeArea.setContentType("text/java");
    }
    else if (language == Language.RHINO) {
      codeArea.setContentType("text/javascript");
    }
    else if (language == Language.BEANSHELL) {
      codeArea.setContentType("text/java");
    }
    
    // set the code
    codeArea.setText(script.getCode());
  }
  
  /**
   * Selects the inputed line number.
   * 
   * @param line  The line number starting with 1.
   */
  public void select(long line) {
    String text = codeArea.getText();
    int position = 0;
    int index = 1;
    while (index < line) {
      position = text.indexOf('\n', position);
      if (position < 0) {
        // error: the line number is invalid
        return;
      }
      index++;
      position++;  // skip '\n'
    }
    codeArea.setCaretPosition(position);
    codeArea.getCaret().setSelectionVisible(true);
    int endPosition = text.indexOf('\n', position);
    if (endPosition < 0) {
      endPosition = text.length();
    }
    codeArea.moveCaretPosition(endPosition);
  }
  
  /**
   * Sets the position of the text insertion caret for the TextComponent. Note that the 
   * caret tracks change, so this may move if the underlying text of the component is 
   * changed. If the document is null, does nothing. The position must be between 0 
   * and the length of the component's text or else an exception is thrown. 
   * 
   * @param position  The position.
   */
  public void setCaretPosition(int position) {
    codeArea.setCaretPosition(position);
  }
}
