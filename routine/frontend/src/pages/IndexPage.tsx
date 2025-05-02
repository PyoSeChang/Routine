// src/pages/IndexPage.tsx
import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import Header2 from '../layout/Header2';
import Footer2 from '../layout/Footer2';

const IndexPage: React.FC = () => {
    useEffect(() => {
        AOS.init({ duration: 1000 });
    }, []);

    return (
        <div className="index-page">
            <Header2 />

            <main>
                <section
                    id="hero"
                    className="hero d-flex align-items-center"
                    style={{
                        backgroundImage: "url('/assets/img/hero-bg.jpg')",
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        minHeight: '100vh',
                    }}
                >
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-lg-10 text-center">
                                <h1 className="mb-3" data-aos="fade-up">Welcome to Dewi</h1>
                                <h2 className="mb-5" data-aos="fade-up" data-aos-delay="200">
                                    우리는 멋진 부트스트랩 템플릿을 React로 가져왔습니다!
                                </h2>
                            </div>
                        </div>
                    </div>
                </section>
            </main>

            <Footer2 />
        </div>
    );
};

export default IndexPage;
