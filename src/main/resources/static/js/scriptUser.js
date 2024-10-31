let currentIndex = 0;
let cardsToShow = 5; // Número inicial de tarjetas visibles
const totalCards = document.querySelectorAll('#slider .card').length;

function setCardsToShow() {
    const windowWidth = window.innerWidth;

    if (windowWidth >= 1200) {
        cardsToShow = 5; // Pantallas grandes
    } else if (windowWidth >= 992) {
        cardsToShow = 4; // Pantallas medianas
    } else if (windowWidth >= 768) {
        cardsToShow = 3; // Pantallas pequeñas
    } else if (windowWidth >= 576) {
        cardsToShow = 2; // Pantallas muy pequeñas
    }else {
		        cardsToShow = 1; // Pantallas muy pequeñas
	}
		}


function updateSliderPosition() {
    const slider = document.getElementById('slider');
    const cardWidth = slider.querySelector('.card').clientWidth;
    slider.style.transform = `translateX(-${currentIndex * cardWidth}px)`;
}

function nextCard() {
    if (currentIndex < totalCards - cardsToShow) {
        currentIndex++;
        updateSliderPosition();
    }
}

function prevCard() {
    if (currentIndex > 0) {
        currentIndex--;
        updateSliderPosition();
    }
}

// Inicializa el número de tarjetas a mostrar y añade listeners
window.addEventListener('load', () => {
    setCardsToShow();
    updateSliderPosition();
});
window.addEventListener('resize', () => {
    setCardsToShow();
    updateSliderPosition();
});
