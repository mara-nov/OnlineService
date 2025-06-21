function upload(type) {
    const input = document.getElementById("fileInput");
    input.value = "";
    input.onchange = () => {
        const file = input.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        if (type === 'work') {
            const name = prompt("Введите название работы:");
            if (!name) return;
            formData.append("name", name);
        }

        const token = document.querySelector('meta[name="_csrf"]').content;
        const header = document.querySelector('meta[name="_csrf_header"]').content;

        fetch(`/group/upload/${type}`, {
            method: "POST",
            headers: {
                [header]: token
            },
            body: formData
        }).then(() => {
             showUploadedFile(type, file.name);
         });
    };
    input.click();
}

function resetAddGroupForm() {
    document.getElementById("groupName").value = "";
    document.getElementById("uploadedFilesList").innerHTML = "";
}

function saveGroup() {
    const name = document.getElementById("groupName").value;
    if (!name) return alert("Введите название группы!");

    const formData = new FormData();
    const projectIdentifier = document.body.dataset.projectId;
    formData.append("identifier", projectIdentifier);
    formData.append("name", name);

    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch("/group/upload/save", {
        method: "POST",
        headers: {
            [header]: token
        },
        body: formData
    })
    .then(response => {
        if (!response.ok) throw new Error("Ошибка при сохранении");
        return response.json();
    })
    .then(group => {
        console.log("Сохранена группа:", group);
        addGroupToSidebar(group);
        resetAddGroupForm();
        window.location.reload();
    })
    .catch(error => {
        console.error("Ошибка:", error);
    });
}

function showUploadedFile(type, fileName, workName) {
    const list = document.getElementById("uploadedFilesList");
    const item = document.createElement("div");

    let label = '';
    if (type === 'tasks') label = 'Условия добавлены ';
    else if (type === 'criteria') label = 'Критерии добавлены ';
    else if (type === 'work') label = `Работа добавлена`;

    item.textContent = `✔ ${label} — ${fileName}`;
    list.appendChild(item);
}

function addGroupToSidebar(group) {
    const sidebar = document.querySelector(".sidebar-left");

    const groupDiv = document.createElement("div");
    groupDiv.classList.add("class-group");


    const groupBtn = document.createElement("button");
    groupBtn.textContent = group.name + " ▼";
    const groupId = "group-" + group.id;
    groupBtn.setAttribute("onclick", `toggleClass('${groupId}')`);


    const workList = document.createElement("div");
    workList.id = groupId;
    workList.classList.add("work-list");
    workList.style.display = "none";


    group.works.forEach(work => {
        const a = document.createElement("a");
        a.href = `/project?workId=${work.id}`;
        a.textContent = work.name;
        a.onclick = showBottomButtons;
        workList.appendChild(a);
    });


    const addBtn = document.createElement("button");
    addBtn.classList.add("dark-blue-button");
    addBtn.setAttribute("onclick", `addWork(${group.id})`);
    addBtn.textContent = "Добавить работу";

    workList.appendChild(addBtn);

    groupDiv.appendChild(groupBtn);
    groupDiv.appendChild(workList);


    const addGroupButton = document.querySelector(".sidebar-left > div:last-child");
    sidebar.insertBefore(groupDiv, addGroupButton);
}

function showAddGroupForm() {
    const workSection = document.querySelector(".center-content iframe")?.parentElement;
    if (workSection) workSection.style.display = 'none';

    const bottomBtns = document.getElementById("bottomButtons");
    if (bottomBtns) bottomBtns.classList.remove("visible");

    const placeholder = document.querySelector(".placeholder");
    if (placeholder) placeholder.style.display = 'none';

    const form = document.getElementById("addGroupForm");
    if (form) form.style.display = 'block';

    const groupName = document.getElementById("groupName");
    if (groupName) groupName.value = '';

    const list = document.getElementById("uploadedFilesList");
    if (list) list.innerHTML = '';
}

function addWork(groupId) {
    const input = document.getElementById("fileInput");
    input.value = "";

    input.onchange = () => {
        const file = input.files[0];
        if (!file) return;

        const name = prompt("Введите название работы:");
        if (!name) return;

        const formData = new FormData();
        formData.append("file", file);
        formData.append("name", name);
        formData.append("groupId", groupId);

        const token = document.querySelector('meta[name="_csrf"]').content;
        const header = document.querySelector('meta[name="_csrf_header"]').content;

        fetch(`/project/${groupId}/work`, {
            method: "POST",
            headers: {
                [header]: token
            },
            body: formData
        }).then(response => {
            if (!response.ok) throw new Error("Ошибка при загрузке");
            return response.json();
        }).then(() => {
            location.reload();
        }).catch(err => {
            alert("Ошибка: " + err.message);
        });
    };

    input.click();
}

function openConditions() {
    const groupConditionsPath = window.conditionsPath;
    if (!groupConditionsPath) {
        alert("Файл условий не найден.");
        return;
    }

    const iframe = document.getElementById("conditionsFrame");

    iframe.src = '/uploads/' + encodeURIComponent(window.conditionsPath);

    const modal = document.getElementById("conditionsModal");
    modal.style.display = "block";

    makeModalDraggable(modal);
    makeModalResizable(modal);
}

function closeConditions() {
    document.getElementById("conditionsModal").style.display = "none";
}

function openCriteria() {
    const groupCriteriaPath = window.criteriaPath;
    if (!groupCriteriaPath) {
        alert("Файл критериев не найден.");
        return;
    }

    const iframe = document.getElementById("conditionsFrame");

    iframe.src = '/uploads/' + encodeURIComponent(window.criteriaPath);

    const modal = document.getElementById("conditionsModal");
    modal.style.display = "block";

    makeModalDraggable(modal);
    makeModalResizable(modal);
}

function closeCriteria() {
    document.getElementById("conditionsModal").style.display = "none";
}

function makeModalDraggable(modal) {
    let isDragging = false, offsetX, offsetY;

    modal.onmousedown = function(e) {
        isDragging = true;
        offsetX = e.clientX - modal.offsetLeft;
        offsetY = e.clientY - modal.offsetTop;
        modal.style.cursor = 'move';
    };

    document.onmouseup = function() {
        isDragging = false;
        modal.style.cursor = 'default';
    };

    document.onmousemove = function(e) {
        if (isDragging) {
            modal.style.left = (e.clientX - offsetX) + 'px';
            modal.style.top = (e.clientY - offsetY) + 'px';
        }
    };
}

function makeModalResizable(modal) {
    const resizeHandle = modal.querySelector('#resizeHandle');
    let isResizing = false;
    let lastX, lastY;

    resizeHandle.addEventListener('mousedown', function(e) {
        e.stopPropagation();
        isResizing = true;
        lastX = e.clientX;
        lastY = e.clientY;
        document.body.style.userSelect = 'none';
    });

    document.addEventListener('mousemove', function(e) {
        if (!isResizing) return;

        const dx = e.clientX - lastX;
        const dy = e.clientY - lastY;

        const newWidth = modal.offsetWidth + dx;
        const newHeight = modal.offsetHeight + dy;


        if (newWidth > 200) modal.style.width = newWidth + 'px';
        if (newHeight > 150) modal.style.height = newHeight + 'px';

        lastX = e.clientX;
        lastY = e.clientY;
    });

    document.addEventListener('mouseup', function() {
        isResizing = false;
        document.body.style.userSelect = '';
    });
}

function selectResultTable(id) {
    alert('Вы выбрали итоговую таблицу с ID: ' + id);
}

function saveCheckTable() {
    const tbody = document.getElementById('checkTableBody');
    const lastRow = tbody.lastElementChild;
    const cells = lastRow.querySelectorAll('td');

    const checkerName = cells[0].innerText.trim();
    const scores = [];

    for (let i = 1; i < cells.length; i++) {
        const val = parseInt(cells[i].innerText.trim());
        scores.push(!isNaN(val) ? val : null);
    }

    if (!checkerName) {
        alert("Введите ФИО проверяющего.");
        return;
    }

    const resultData = [{
        checkerName: checkerName,
        scores: scores
    }];

    const workId = document.body.getAttribute('data-work-id');
    if (!workId) {
        alert("Работа не выбрана. Сначала выберите работу слева.");
        return;
    }

    fetch('/project/save-check?workId=' + workId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [document.querySelector('meta[name="_csrf_header"]').content]:
                document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify(resultData)
    })
    .then(resp => {
        if (!resp.ok) throw new Error('Ошибка при сохранении');
        return resp.text();
    })
    .then(msg => {
        alert('Сохранено: ' + msg);
        closeCheckTable();
    })
    .catch(err => {
        alert('Ошибка: ' + err.message);
    });
}

function openCheckTable() {
    document.getElementById('checkTableModal').style.display = 'flex';

    const tbody = document.getElementById('checkTableBody');
    tbody.innerHTML = '';

    const workId = document.body.getAttribute('data-work-id');
    if (!workId) return;

    fetch(`/project/check-results?workId=${workId}`)
        .then(resp => resp.json())
        .then(data => {
            if (data.length === 0) {
                addCheckTableRow();
                return;
            }

            for (const row of data) {
                const tr = document.createElement('tr');
                const tdName = document.createElement('td');
                tdName.contentEditable = 'true';
                tdName.innerText = row.checkerName || '';
                tdName.style.minWidth = '120px';
                tr.appendChild(tdName);

                for (let i = 0; i < 7; i++) {
                    const td = document.createElement('td');
                    td.contentEditable = 'true';
                    td.innerText = row.scores?.[i] ?? '';
                    tr.appendChild(td);
                }

                tbody.appendChild(tr);
            }
        })
        .catch(err => {
            console.error('Ошибка загрузки результатов проверки:', err);
            addCheckTableRow();
        });
}




window.showAddGroupForm = showAddGroupForm;
window.addEventListener("DOMContentLoaded", () => {
    const selected = document.querySelector("a.active");
    if (selected) {
        const workList = selected.closest(".work-list");
        if (workList) {
            workList.style.display = "block";
        }
    }
});