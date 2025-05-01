/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors');

module.exports = {
    content: [
        "./src/**/*.{js,jsx,ts,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                note: '#fff9c4',
            },
            fontFamily: {
                user: ['Dokrip', 'sans-serif'],
                ui: ['MaruBuri', 'sans-serif'],
            },
            fontSize: {
                base: '20px', // 기본 base 자체를 20px로 바꿔도 됨
            },
            backgroundImage: {
                'sticky-green': "url('../public/assets/bg/green-postIt.png')",
                // 'corc': "url('../public/assets/bg/d.png')",
            },
        },
    },
    plugins: [],
};
