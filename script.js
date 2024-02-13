document.querySelectorAll(".copy-button").forEach((button) => {
    button.addEventListener("click", function() {
      const pre = button.closest("pre");
      const code = pre.querySelector("code");
      const range = document.createRange();
      range.selectNode(code);
      const selection = window.getSelection();
      selection.removeAllRanges();
      selection.addRange(range);
      document.execCommand("copy");
      selection.removeAllRanges();
    });
  });
  