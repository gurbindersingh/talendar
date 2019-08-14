interface IRecaptchaRenderParams {
    sitekey: string;
    theme: string;
    tabindex: number;
    callback: (...args: any[]) => any;
    'expired-callback': (...args: any[]) => any;
    'error-callback': (...args: any[]) => any;
}

// Adds grecaptcha to 'window' object
interface Window {
    grecaptcha: {
        render: (container: string, parameters: IRecaptchaRenderParams) => any;
        reset: (opt_widget_id?: string) => any;
        getResponse: (opt_widget_id?: string) => string;
    };
}
