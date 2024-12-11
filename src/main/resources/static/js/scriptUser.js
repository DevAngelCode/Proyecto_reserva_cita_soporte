let currentIndex = 0;
let cardsToShow = 5; // Número inicial de tarjetas visibles
const totalCards = document.querySelectorAll('#slider .carddd').length;

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
    } else {
        cardsToShow = 1; // Pantallas muy pequeñas
    }
}


/*function updateSliderPosition() {
    const slider = document.getElementById('slider');
    const cardWidth = slider.querySelector('.card').clientWidth;
    slider.style.transform = `translateX(-${currentIndex * cardWidth}px)`;
}*/
function updateSliderPosition() {
    const slider = document.getElementById('slider');
    
    if (slider) {
        const card = slider.querySelector('.carddd');
        
        if (card) {
            const cardWidth = card.clientWidth;
            slider.style.transform = `translateX(-${currentIndex * cardWidth}px)`;
        } else {
            console.error("No se encontraron tarjetas dentro del slider.");
        }
    } else {
        console.error("No se encontró el slider.");
    }
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

//#region Inicializa el número de tarjetas a mostrar y añade listeners
window.addEventListener('load', () => {
    setCardsToShow();
    updateSliderPosition();
});
window.addEventListener('resize', () => {
    setCardsToShow();
    updateSliderPosition();
});

//#region SCRIPT PARA EL FORM DE LOGIN Y REGISTER 
var x = document.getElementById("login");
var y = document.getElementById("registrar");
var z = document.getElementById("elegir");

function login() {
    x.style.left = "50px";
    y.style.left = "450px";
    z.style.left = "0px";
}

function registrar() {
    x.style.left = "-400px";
    y.style.left = "50px";
    z.style.left = "135px";
}


//#region CONTROLAR EL FORMULARIO DE CITAS
const prevBtns = document.querySelectorAll(".btn-prev");
const nextBtns = document.querySelectorAll(".btn-next");
const progress = document.getElementById("progress");
const formSteps = document.querySelectorAll(".form-step");
const progressSteps = document.querySelectorAll(".progress-step");

let formStepsNum = 0;

nextBtns.forEach((btn) => {
    btn.addEventListener("click", () => {
        formStepsNum++;
        updateFormSteps();
        updateProgressbar();
    });
});

prevBtns.forEach((btn) => {
    btn.addEventListener("click", () => {
        formStepsNum--;
        updateFormSteps();
        updateProgressbar();
    });
});

function updateFormSteps() {
    formSteps.forEach((formStep) => {
        formStep.classList.contains("form-step-active") &&
            formStep.classList.remove("form-step-active");
    });

    formSteps[formStepsNum].classList.add("form-step-active");
}

function updateProgressbar() {
    progressSteps.forEach((progressStep, idx) => {
        if (idx < formStepsNum + 1) {
            progressStep.classList.add("progress-step-active");
        } else {
            progressStep.classList.remove("progress-step-active");
        }
    });

    const progressActive = document.querySelectorAll(".progress-step-active");

    progress.style.width =
        ((progressActive.length - 1) / (progressSteps.length - 1)) * 100 + "%";
}


const textarea = document.getElementById('textDesc');
const maxRows = 3; // Establece el número máximo de filas permitidas

