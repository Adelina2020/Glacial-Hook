import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

public class EffectGroup
  {
    private List<Effect> effects;

    public EffectGroup() {
      effects = new ArrayList<Effect>();
    }

    public void add(Effect ef) {
      effects.add(ef);
    }

    public List<Effect> getList() {
      return effects;
    }

    public void clear() {
      effects.clear();
    }

    public void draw(Graphics window) {
      for (Effect ef : effects) {
        ef.draw(window);
      }
    }

    public void updateEffects() {
      for (Effect ef : effects) {
        ef.updateFrame();
      }
      
      for (int i = 0; i < effects.size(); i++) {
        if (effects.size() == 0) {
          return;
        }
        
        if (effects.get(i).isOver()) {
          effects.remove(i);
          i--;
          if (i < 0) {
            i = 0;
          }
        }
        
      }
    }
  }