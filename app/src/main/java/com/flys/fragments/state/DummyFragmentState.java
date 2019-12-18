package com.flys.fragments.state;

import com.flys.architecture.custom.CoreState;

public class DummyFragmentState extends CoreState {
  // état du fragment [DummyFragment]
  // ne mettre que des champs sérialisables en jSON
  // mettre l'annotation @JsonIgnore sur les autres mais on voit mal à quoi ils pourraient servir
  // ne pas oublier les getters / setters - ils sont utilisés pour la sérialisation / désérialisation
}
