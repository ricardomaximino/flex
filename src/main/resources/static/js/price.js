    const costInput = document.getElementById('price.cost');
    const taxInput = document.getElementById('price.tax');
    const marginSlider = document.getElementById('marginSlider');
    const marginValue = document.getElementById('price.marginValue');
    const finalPrice = document.getElementById('price.finalPrice');
    const finalInput = document.getElementById('finalInput');
    const calculatedMargin = document.getElementById('calculatedMargin');

    const marginSection = document.getElementById('marginSection');
    const finalSection = document.getElementById('finalSection');

    document.querySelectorAll('input[name="priceMode"]').forEach(radio => {
      radio.addEventListener('change', () => {
        if (radio.value === 'margin') {
          marginSection.style.display = 'block';
          finalSection.style.display = 'none';
        } else {
          marginSection.style.display = 'none';
          finalSection.style.display = 'block';
        }
      });
    });

    function updateFinalPrice() {
      const cost = parseFloat(costInput.value) || 0;
      const tax = parseFloat(taxInput.value) || 0;
      const margin = parseFloat(marginSlider.value) || 0;

      marginValue.textContent = margin;
      const taxedCost = cost * (1 + tax / 100);
      const price = taxedCost * (1 + margin / 100);
      finalPrice.textContent = price.toFixed(2);
    }

    function updateMarginFromFinal() {
      const cost = parseFloat(costInput.value) || 0;
      const tax = parseFloat(taxInput.value) || 0;
      const final = parseFloat(finalInput.value) || 0;

      const taxedCost = cost * (1 + tax / 100);
      const margin = ((final / taxedCost) - 1) * 100;
      calculatedMargin.textContent = margin.toFixed(2);
    }

    marginSlider.addEventListener('input', updateFinalPrice);
    costInput.addEventListener('input', () => {
      updateFinalPrice();
      updateMarginFromFinal();
    });
    taxInput.addEventListener('input', () => {
      updateFinalPrice();
      updateMarginFromFinal();
    });
    finalInput.addEventListener('input', updateMarginFromFinal);

    updateFinalPrice(); // Initial calculation