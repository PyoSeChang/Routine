/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors');

module.exports = {
    content: [
        "./src/**/*.{js,jsx,ts,tsx}",
        "./public/**/*.{js,jsx,ts,tsx, png}",
    ],
    theme: {
        extend: {
            colors: {
                note: '#fff9c4',
                mainBlue: '#1e40af',
                mainGreen: '#166534',
                mainRed: '#991b1b',
                brown: '#9f5c1d',
                'postit-yellow': '#fff9c4',
                'postit-orange': '#ffe0b2',
                'postit-pink':   '#f8bbd0',
                'postit-green':  '#c8e6c9',
                'postit-blue':   '#b3e5fc',
                'postit-purple': '#d1c4e9',
                'postit-mint':   '#ccf2f4',
                mainYellow: '#FFFACD',
            },
            fontFamily: {
                user: ['Dokrip', 'sans-serif'],
                ui: ['Nanum Gothic', 'MaruBuri'],
                chalk: ['Cafe24Decoschool', 'MaruBuri'],
            },
            fontSize: {
                base: '20px', // 기본 base 자체를 20px로 바꿔도 됨
            },
            backgroundImage: {
                'sticky-green': "url/assets/bg/green-postIt.png')",
                'corc': "url/assets/bg/d.png')",
                'design': "url/assets/bg/1분전만큼",
            },
        },
    },
    plugins: [],
};
