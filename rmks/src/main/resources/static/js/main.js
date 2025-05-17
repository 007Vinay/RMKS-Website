// Language Switcher
function switchLanguage(lang) {
    document.cookie = "preferredLanguage=" + lang + ";path=/;max-age=31536000"; // 1 year
    location.reload();
}

// Form Validation
document.addEventListener('DOMContentLoaded', function() {
    // Get all forms that need validation
    const forms = document.querySelectorAll('.needs-validation');

    // Loop over them and prevent submission
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // File input validation
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        input.addEventListener('change', function() {
            const file = this.files[0];
            if (file) {
                // Check file size (max 5MB)
                if (file.size > 5 * 1024 * 1024) {
                    alert('File size should not exceed 5MB');
                    this.value = '';
                    return;
                }

                // Check file type
                const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
                if (!allowedTypes.includes(file.type)) {
                    alert('Only JPG, PNG & GIF files are allowed');
                    this.value = '';
                    return;
                }
            }
        });
    });
});

// Rich Text Editor Initialization
document.addEventListener('DOMContentLoaded', function() {
    const richTextAreas = document.querySelectorAll('.rich-text-editor');
    richTextAreas.forEach(area => {
        if (typeof tinymce !== 'undefined') {
            tinymce.init({
                selector: '#' + area.id,
                height: 300,
                menubar: false,
                plugins: [
                    'advlist autolink lists link image charmap print preview anchor',
                    'searchreplace visualblocks code fullscreen',
                    'insertdatetime media table paste code help wordcount'
                ],
                toolbar: 'undo redo | formatselect | bold italic backcolor | \
                    alignleft aligncenter alignright alignjustify | \
                    bullist numlist outdent indent | removeformat | help'
            });
        }
    });
});

// Dynamic Form Fields
function addFormField(containerId, template) {
    const container = document.getElementById(containerId);
    const newField = template.cloneNode(true);
    newField.classList.remove('d-none');
    container.appendChild(newField);
}

function removeFormField(button) {
    button.closest('.form-group').remove();
}

// Image Preview
function previewImage(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const preview = document.querySelector('#imagePreview');
            if (preview) {
                preview.src = e.target.result;
                preview.classList.remove('d-none');
            }
        };
        reader.readAsDataURL(input.files[0]);
    }
}

// Confirmation Dialogs
function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete this item?');
}

// AJAX Form Submission
function submitFormAjax(formId, successCallback, errorCallback) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = new FormData(form);

        fetch(form.action, {
            method: form.method,
            body: formData,
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (successCallback) successCallback(data);
        })
        .catch(error => {
            if (errorCallback) errorCallback(error);
            else console.error('Error:', error);
        });
    });
}

// Responsive Navigation
document.addEventListener('DOMContentLoaded', function() {
    const navbarToggler = document.querySelector('.navbar-toggler');
    if (navbarToggler) {
        navbarToggler.addEventListener('click', function() {
            const target = document.querySelector(this.dataset.bsTarget);
            if (target) {
                target.classList.toggle('show');
            }
        });
    }
});

// Scroll to Top Button
window.onscroll = function() {
    const scrollButton = document.getElementById('scrollToTop');
    if (scrollButton) {
        if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
            scrollButton.style.display = 'block';
        } else {
            scrollButton.style.display = 'none';
        }
    }
};

function scrollToTop() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}

// News Filter
function filterNews(category) {
    const newsItems = document.querySelectorAll('.news-item');
    newsItems.forEach(item => {
        if (category === 'all' || item.dataset.category === category) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}

// Form Auto Save
let autoSaveTimeout;
function setupAutoSave(formId, saveEndpoint) {
    const form = document.getElementById(formId);
    if (!form) return;

    const formInputs = form.querySelectorAll('input, textarea, select');
    formInputs.forEach(input => {
        input.addEventListener('change', () => {
            clearTimeout(autoSaveTimeout);
            autoSaveTimeout = setTimeout(() => {
                const formData = new FormData(form);
                fetch(saveEndpoint, {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.json())
                .then(data => {
                    console.log('Auto saved:', data);
                })
                .catch(error => console.error('Auto save failed:', error));
            }, 1000);
        });
    });
}

// Initialize Bootstrap Tooltips and Popovers
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
});