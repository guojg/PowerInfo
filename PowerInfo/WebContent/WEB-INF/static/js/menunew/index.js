var buttons = document.querySelectorAll(".radmenu a");

  var button = buttons[0];
  button.onclick = setSelected;


function setSelected(e) {
    if (this.classList.contains("selected")) {
      this.classList.remove("selected");
      if (!this.parentNode.classList.contains("radmenu")) {
        this.parentNode.parentNode.parentNode.querySelector("a").classList.add("selected")
      } else {
        this.classList.add("show");
      }
    } else {
      this.classList.add("selected");
      if (!this.parentNode.classList.contains("radmenu")) {
        this.parentNode.parentNode.parentNode.querySelector("a").classList.remove("selected")
      } else {
        this.classList.remove("show");
      }
    }
    return false;
}