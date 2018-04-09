package V2VM.elements;

import V2VM.CFG.CFG;

public interface Element {
    String tab = "  ";
    String toVapor(CFG cfg);
}
